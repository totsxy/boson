package org.boson.strategy.impl;

import org.boson.domain.dto.SocialTokenDto;
import org.boson.domain.dto.SocialUserInfoDto;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.po.UserAuth;
import org.boson.domain.po.UserRole;
import org.boson.domain.dto.UserDetailDto;
import org.boson.domain.po.UserInfo;
import org.boson.enums.RoleEnum;
import org.boson.exception.BizException;
import org.boson.service.UserAuthService;
import org.boson.service.UserInfoService;
import org.boson.service.UserRoleService;
import org.boson.service.impl.UserDetailsServiceImpl;
import org.boson.strategy.SocialLoginStrategy;
import org.boson.util.BeanUtils;
import org.boson.util.IpUtils;
import org.boson.enums.ZoneEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static org.boson.constant.CommonConstants.TRUE;


/**
 * 第三方登录抽象模板
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private HttpServletRequest request;

    @Override
    public UserInfoDto login(String data) {
        // 获取第三方token信息
        SocialTokenDto socialToken = this.getSocialToken(data);

        // 获取用户ip信息
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);

        UserDetailDto userDetailDto;
        UserAuth userAuth = this.getUserAuth(socialToken);

        // 判断是否已注册
        if (Objects.nonNull(userAuth)) {
            // 返回数据库用户信息
            userDetailDto = this.getUserDetail(userAuth, ipAddress, ipSource);
        } else {
            // 获取第三方用户信息，保存到数据库返回
            userDetailDto = this.saveUserDetail(socialToken, ipAddress, ipSource);
        }

        // 判断账号是否禁用
        if (userDetailDto.getIsDisable().equals(TRUE)) {
            throw new BizException("账号已被禁用");
        }

        // 将登录信息放入springSecurity管理
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetailDto, null, userDetailDto.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return BeanUtils.bean2Bean(userDetailDto, UserInfoDto.class);
    }

    /**
     * 获取第三方token信息
     *
     * @param data 数据
     * @return {@link SocialTokenDto} 第三方token信息
     */
    public abstract SocialTokenDto getSocialToken(String data);

    /**
     * 获取第三方用户信息
     *
     * @param socialTokenDto 第三方token信息
     * @return {@link SocialUserInfoDto} 第三方用户信息
     */
    public abstract SocialUserInfoDto getSocialUserInfo(SocialTokenDto socialTokenDto);

    /**
     * 获取用户账号
     *
     * @param socialTokenDto 第三方token信息
     * @return {@link UserAuth} 用户账号
     */
    private UserAuth getUserAuth(SocialTokenDto socialTokenDto) {
        return this.userAuthService.beginQuery()
                .eq(UserAuth::getUsername, socialTokenDto.getOpenId())
                .eq(UserAuth::getLoginType, socialTokenDto.getLoginType())
                .getOne();
    }

    /**
     * 获取用户信息
     *
     * @param userAuth  用户账户信息
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailDto} 用户信息
     */
    private UserDetailDto getUserDetail(UserAuth userAuth, String ipAddress, String ipSource) {
        // 更新登录信息
        this.userAuthService.beginUpdate()
                .set(UserAuth::getLastLoginTime, LocalDateTime.now())
                .set(UserAuth::getIpAddress, ipAddress)
                .set(UserAuth::getIpSource, ipSource)
                .eq(UserAuth::getId, userAuth.getId())
                .update();

        // 封装信息
        return this.userDetailsService.convertUserDetail(userAuth, request);
    }

    /**
     * 新增用户信息
     *
     * @param socialTokenDto 第三方token信息
     * @param ipAddress      ip地址
     * @param ipSource       ip源
     * @return {@link UserDetailDto} 用户信息
     */
    private UserDetailDto saveUserDetail(SocialTokenDto socialTokenDto, String ipAddress, String ipSource) {
        // 获取第三方用户信息
        SocialUserInfoDto socialUserInfo = this.getSocialUserInfo(socialTokenDto);

        // 保存用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .build();

        this.userInfoService.save(userInfo);

        // 保存账户信息
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(socialTokenDto.getOpenId())
                .password(socialTokenDto.getAccessToken())
                .loginType(socialTokenDto.getLoginType())
                .lastLoginTime(LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())))
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .build();

        this.userAuthService.save(userAuth);

        // 绑定角色
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();

        this.userRoleService.save(userRole);
        return this.userDetailsService.convertUserDetail(userAuth, request);
    }
}
