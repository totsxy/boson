package org.boson.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.PageResult;
import org.boson.domain.po.UserRole;
import org.boson.service.UserInfoService;
import org.boson.service.UserRoleService;
import org.boson.strategy.context.UploadStrategyContext;
import org.boson.domain.dto.UserDetailDto;
import org.boson.domain.dto.UserOnlineDto;
import org.boson.domain.po.UserInfo;
import org.boson.mapper.UserInfoMapper;
import org.boson.enums.FilePathEnum;
import org.boson.exception.BizException;
import org.boson.service.RedisService;

import org.boson.domain.vo.*;
import org.boson.support.mybatisplus.service.BaseServiceImpl;
import org.boson.util.PageUtils;
import org.boson.util.UserUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.boson.constant.RedisPrefixConst.USER_CODE_KEY;


/**
 * 用户信息服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final UploadStrategyContext uploadStrategyContext;
    private final RedisService redisService;
    private final UserRoleService userRoleService;
    private final SessionRegistry sessionRegistry;

    public UserInfoServiceImpl(UploadStrategyContext uploadStrategyContext,
                               RedisService redisService,
                               UserRoleService userRoleService,
                               SessionRegistry sessionRegistry) {
        this.uploadStrategyContext = uploadStrategyContext;
        this.redisService = redisService;
        this.userRoleService = userRoleService;
        this.sessionRegistry = sessionRegistry;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserInfo(UserInfoVo userInfoVo) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .nickname(userInfoVo.getNickname())
                .intro(userInfoVo.getIntro())
                .webSite(userInfoVo.getWebSite())
                .build();

        return this.updateById(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新头像
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .avatar(avatar)
                .build();

        return this.updateById(userInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveUserEmail(EmailVo emailVo) {
        if (!emailVo.getCode().equals(redisService.get(USER_CODE_KEY + emailVo.getEmail()).toString())) {
            throw new BizException("验证码错误！");
        }

        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .email(emailVo.getEmail())
                .build();

        return this.updateById(userInfo);
    }

    // TODO 优化回滚
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserRole(UserRoleVo userRoleVo) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVo.getUserInfoId())
                .nickname(userRoleVo.getNickname())
                .build();

        if (!this.updateById(userInfo)) {
            return false;
        }

        // 删除用户角色重新添加
        boolean removed = this.userRoleService.beginQuery()
                .eq(UserRole::getUserId, userRoleVo.getUserInfoId())
                .remove();

        // 失败时触发回滚
        if (!removed) {
            throw new BizException("删除用户角色失败");
        }

        List<UserRole> userRoleList = userRoleVo.getRoleIdList()
                .stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVo.getUserInfoId())
                        .build())
                .collect(Collectors.toList());

        // 失败时触发回滚
        if (!userRoleService.saveBatch(userRoleList)) {
            throw new BizException("更新用户角色失败");
        }

        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUserDisable(UserDisableVo userDisableVo) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(userDisableVo.getId())
                .isDisable(userDisableVo.getIsDisable())
                .build();
        return this.updateById(userInfo);
    }

    @Override
    public PageResult<UserOnlineDto> listOnlineUsers(ConditionVo conditionVo) {
        // 获取security在线session
        List<UserOnlineDto> userOnlineDtoList = sessionRegistry.getAllPrincipals()
                .stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDto.class))
                .filter(item -> StringUtils.isBlank(conditionVo.getKeywords()) || item.getNickname().contains(conditionVo.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineDto::getLastLoginTime).reversed())
                .collect(Collectors.toList());

        // 执行分页
        int fromIndex = PageUtils.getLimitCurrent().intValue();
        int size = PageUtils.getSize().intValue();
        int toIndex = userOnlineDtoList.size() - fromIndex > size ? fromIndex + size : userOnlineDtoList.size();
        List<UserOnlineDto> userOnlineList = userOnlineDtoList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, userOnlineDtoList.size());
    }

    @Override
    public void removeOnlineUser(Integer userInfoId) {
        // 获取用户session
        List<Object> userInfoList = sessionRegistry.getAllPrincipals()
                .stream()
                .filter(it -> ((UserDetailDto) it).getUserInfoId().equals(userInfoId))
                .collect(Collectors.toList());

        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(it -> allSessions.addAll(sessionRegistry.getAllSessions(it, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }
}
