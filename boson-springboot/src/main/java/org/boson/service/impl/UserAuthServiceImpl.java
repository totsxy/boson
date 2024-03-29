package org.boson.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.PageResult;
import org.boson.domain.dto.EmailDto;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.po.UserAuth;
import org.boson.domain.po.UserRole;
import org.boson.domain.vo.*;
import org.boson.enums.UserAreaTypeEnum;
import org.boson.mapper.UserAuthMapper;
import org.boson.domain.po.UserInfo;
import org.boson.enums.LoginTypeEnum;
import org.boson.enums.RoleEnum;
import org.boson.exception.BizException;
import org.boson.service.RedisService;
import org.boson.service.UserAuthService;
import org.boson.service.UserInfoService;
import org.boson.service.UserRoleService;
import org.boson.strategy.context.SocialLoginStrategyContext;
import org.boson.domain.dto.UserAreaDto;
import org.boson.domain.dto.UserBackDto;
import org.boson.support.service.BaseServiceImpl;
import org.boson.util.CommonUtils;
import org.boson.util.PageUtils;
import org.boson.util.UserUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.*;
import java.util.stream.Collectors;

import static org.boson.constant.CommonConstants.*;
import static org.boson.constant.MQPrefixConstants.EMAIL_EXCHANGE;
import static org.boson.constant.RedisPrefixConstants.*;


/**
 * 用户账户服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class UserAuthServiceImpl extends BaseServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SocialLoginStrategyContext socialLoginStrategyContext;

    @Override
    public void sendCode(String username) {
        // 校验账号是否合法
        if (!CommonUtils.checkEmail(username)) {
            throw new BizException("请输入正确邮箱");
        }

        // 生成六位随机验证码发送
        String code = CommonUtils.getRandomCode();
        EmailDto emailDto = EmailDto.builder()
                .to(username)
                .subject("验证码")
                .content("您的验证码为 " + code + " 有效期15分钟，请不要告诉他人哦！")
                .build();

        this.rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, "*", new Message(JSON.toJSONBytes(emailDto), new MessageProperties()));
        this.redisService.set(USER_CODE_KEY + username, code, CODE_EXPIRE_TIME);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean register(UserVo userVo) {
        // 校验账号是否合法
        if (this.checkUser(userVo)) {
            throw new BizException("邮箱已被注册");
        }

        // 新增用户信息
        UserInfo userInfo = UserInfo.builder()
                .email(userVo.getUsername())
                .nickname(DEFAULT_NICKNAME + IdWorker.getId())
                // TODO add avatar
//                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
                .build();

        if (!this.userInfoService.save(userInfo)) {
            return false;
        }

        // 绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();

        if (!this.userRoleService.save(userRole)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        // 新增用户账号
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(userVo.getUsername())
                .password(BCrypt.hashpw(userVo.getPassword(), BCrypt.gensalt()))
                .loginType(LoginTypeEnum.EMAIL.getType())
                .build();

        if (!this.save(userAuth)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }

    @Override
    public boolean updatePassword(UserVo userVo) {
        // 校验账号是否合法
        if (!this.checkUser(userVo)) {
            throw new BizException("邮箱尚未注册");
        }

        // 根据用户名修改密码
        return this.beginUpdate()
                .set(UserAuth::getPassword, BCrypt.hashpw(userVo.getPassword(), BCrypt.gensalt()))
                .eq(UserAuth::getUsername, userVo.getUsername())
                .update();
    }

    @Override
    public boolean updateAdminPassword(PasswordVo passwordVo) {
        // 查询旧密码是否正确
        UserAuth userAuth = this.beginQuery()
                .eq(UserAuth::getId, UserUtils.getLoginUser().getId())
                .getOne();

        if (Objects.isNull(userAuth)) {
            throw new BizException("登录账户缺失");
        }

        // 正确则修改密码，错误则提示不正确
        if (BCrypt.checkpw(passwordVo.getOldPassword(), userAuth.getPassword())) {
            throw new BizException("旧密码不正确");
        }

        UserAuth newUserAuth = UserAuth.builder()
                .id(UserUtils.getLoginUser().getId())
                .password(BCrypt.hashpw(passwordVo.getNewPassword(), BCrypt.gensalt()))
                .build();

        return this.updateById(newUserAuth);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDto qqLogin(QQLoginVo qqLoginVo) {
        return this.socialLoginStrategyContext.executeLoginStrategy(LoginTypeEnum.QQ, JSON.toJSONString(qqLoginVo));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserInfoDto weiboLogin(WeiboLoginVo weiboLoginVo) {
        return this.socialLoginStrategyContext.executeLoginStrategy(LoginTypeEnum.WEIBO, JSON.toJSONString(weiboLoginVo));
    }

    @Override
    public PageResult<UserBackDto> pageUserBacks(ConditionVo conditionVo) {
        // 获取后台用户数量
        Integer count = this.getBaseMapper().countUser(conditionVo);
        if (count == 0) {
            return new PageResult<>();
        }

        // 获取后台用户列表
        List<UserBackDto> userBackDtoList = this.getBaseMapper().pageUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        return new PageResult<>(userBackDtoList, count);
    }

    @Override
    public List<UserAreaDto> listUserAreas(ConditionVo conditionVo) {
        List<UserAreaDto> userAreaDtoList = new ArrayList<>(0);
        switch (Objects.requireNonNull(UserAreaTypeEnum.getUserAreaType(conditionVo.getType()))) {
            case USER:
                // 查询注册用户区域分布
                Object userArea = this.redisService.get(USER_AREA);
                if (Objects.nonNull(userArea)) {
                    userAreaDtoList = JSON.parseObject(userArea.toString(), List.class);
                }
            case VISITOR:
                // 查询游客区域分布
                Map<String, Object> visitorAreaMap = this.redisService.hGetAll(VISITOR_AREA);
                if (Objects.nonNull(visitorAreaMap)) {
                    userAreaDtoList = visitorAreaMap.entrySet()
                            .stream()
                            .map(it -> UserAreaDto.builder()
                                    .name(it.getKey())
                                    .value(Long.valueOf(it.getValue().toString()))
                                    .build())
                            .collect(Collectors.toList());
                }
            default:
                break;
        }

        return userAreaDtoList;
    }

    private Boolean checkUser(UserVo userVo) {
        if (!userVo.getCode().equals(redisService.get(USER_CODE_KEY + userVo.getUsername()))) {
            throw new BizException("验证码错误！");
        }

        //查询用户名是否存在
        UserAuth userAuth = this.beginQuery()
                .select(UserAuth::getUsername)
                .eq(UserAuth::getUsername, userVo.getUsername())
                .getOne();

        return Objects.nonNull(userAuth);
    }

    /**
     * 统计用户地区
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void statisticalUserArea() {
        // 统计用户地域分布
        Map<String, Long> userAreaMap = this.beginQuery()
                .select(UserAuth::getIpSource)
                .queryList()
                .stream()
                .map(userAuth -> {
                    if (StringUtils.isNotBlank(userAuth.getIpSource())) {
                        return userAuth.getIpSource()
                                .substring(0, 2)
                                .replaceAll(PROVINCE, "")
                                .replaceAll(CITY, "");
                    }
                    return UNKNOWN;
                })
                .collect(Collectors.groupingBy(it -> it, Collectors.counting()));

        // 转换格式
        List<UserAreaDto> userAreaDtoList = userAreaMap.entrySet()
                .stream()
                .map(it -> UserAreaDto.builder()
                        .name(it.getKey())
                        .value(it.getValue())
                        .build())
                .collect(Collectors.toList());

        redisService.set(USER_AREA, JSON.toJSONString(userAreaDtoList));
    }
}
