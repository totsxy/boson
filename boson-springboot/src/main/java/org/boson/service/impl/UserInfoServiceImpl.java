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
import org.boson.support.service.BaseServiceImpl;
import org.boson.util.PageUtils;
import org.boson.util.UserUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.boson.constant.RedisPrefixConstants.USER_CODE_KEY;


/**
 * 用户信息服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    private final RedisService redisService;

    private final UserRoleService userRoleService;

    private final SessionRegistry sessionRegistry;

    private final UploadStrategyContext uploadStrategyContext;

    public UserInfoServiceImpl(RedisService redisService,
                               UserRoleService userRoleService,
                               SessionRegistry sessionRegistry,
                               UploadStrategyContext uploadStrategyContext) {
        this.redisService = redisService;
        this.userRoleService = userRoleService;
        this.sessionRegistry = sessionRegistry;
        this.uploadStrategyContext = uploadStrategyContext;
    }

    @Override
    public boolean updateUserInfo(UserInfoVo userInfoVo) {
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .nickname(userInfoVo.getNickname())
                .intro(userInfoVo.getIntro())
                .webSite(userInfoVo.getWebSite())
                .build();

        return this.updateById(userInfo);
    }

    @Override
    public String updateUserAvatar(MultipartFile file) {
        // 头像上传
        String avatar = this.uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.AVATAR.getPath());
        // 更新头像
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .avatar(avatar)
                .build();

        if (!this.updateById(userInfo)) {
            throw new BizException("更新用户头像失败");
        }

        return avatar;
    }

    @Override
    public boolean saveUserEmail(EmailVo emailVo) {
        if (!emailVo.getCode().equals(this.redisService.get(USER_CODE_KEY + emailVo.getEmail()).toString())) {
            throw new BizException("验证码错误");
        }

        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .email(emailVo.getEmail())
                .build();

        return this.updateById(userInfo);
    }

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
        if (!this.userRoleService.removeByUserInfoId(userRoleVo.getUserInfoId())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        List<UserRole> userRoleList = userRoleVo.getRoleIdList()
                .stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVo.getUserInfoId())
                        .build())
                .collect(Collectors.toList());

        if (!this.userRoleService.saveBatch(userRoleList)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }

        return true;
    }

    @Override
    public boolean updateUserDisable(UserDisableVo userDisableVo) {
        UserInfo userInfo = UserInfo.builder()
                .id(userDisableVo.getId())
                .isDisable(userDisableVo.getIsDisable())
                .build();

        return this.updateById(userInfo);
    }

    @Override
    public PageResult<UserOnlineDto> listOnlineUsers(ConditionVo conditionVo) {
        // 获取security在线session
        List<UserOnlineDto> userOnlineDtoList = this.sessionRegistry.getAllPrincipals()
                .stream()
                .filter(it -> this.sessionRegistry.getAllSessions(it, false).size() > 0)
                .map(it -> JSON.parseObject(JSON.toJSONString(it), UserOnlineDto.class))
                .filter(it -> StringUtils.isBlank(conditionVo.getKeywords()) || it.getNickname().contains(conditionVo.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineDto::getLastLoginTime).reversed())
                .collect(Collectors.toList());

        return PageUtils.limit(userOnlineDtoList);
    }

    @Override
    public void removeOnlineUser(Integer userInfoId) {
        // 获取用户session
        List<Object> userInfoList = this.sessionRegistry.getAllPrincipals()
                .stream()
                .filter(it -> ((UserDetailDto) it).getUserInfoId().equals(userInfoId))
                .collect(Collectors.toList());

        List<SessionInformation> allSessions = new ArrayList<>();
        userInfoList.forEach(it -> allSessions.addAll(sessionRegistry.getAllSessions(it, false)));
        allSessions.forEach(SessionInformation::expireNow);
    }
}
