package org.boson.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.boson.constant.CommonConstants;
import org.boson.domain.dto.SocialTokenDto;
import org.boson.domain.dto.SocialUserInfoDto;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.po.UserAuth;
import org.boson.domain.po.UserRole;
import org.boson.mapper.UserAuthMapper;
import org.boson.mapper.UserInfoMapper;
import org.boson.mapper.UserRoleMapper;
import org.boson.domain.dto.UserDetailDto;
import org.boson.domain.po.UserInfo;
import org.boson.enums.RoleEnum;
import org.boson.exception.BizException;
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


/**
 * 第三方登录抽象模板
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Service
public abstract class AbstractSocialLoginStrategyImpl implements SocialLoginStrategy {
    @Autowired
    private UserAuthMapper userAuthMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Resource
    private HttpServletRequest request;

    @Override
    public UserInfoDto login(String data) {
        // 创建登录信息
        UserDetailDto userDetailDTO;
        // 获取第三方token信息
        SocialTokenDto socialToken = getSocialToken(data);
        // 获取用户ip信息
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 判断是否已注册
        UserAuth user = getUserAuth(socialToken);
        if (Objects.nonNull(user)) {
            // 返回数据库用户信息
            userDetailDTO = getUserDetail(user, ipAddress, ipSource);
        } else {
            // 获取第三方用户信息，保存到数据库返回
            userDetailDTO = saveUserDetail(socialToken, ipAddress, ipSource);
        }
        // 判断账号是否禁用
        if (userDetailDTO.getIsDisable().equals(CommonConstants.TRUE)) {
            throw new BizException("账号已被禁用");
        }
        // 将登录信息放入springSecurity管理
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetailDTO, null, userDetailDTO.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        // 返回用户信息
        return BeanUtils.bean2Bean(userDetailDTO, UserInfoDto.class);
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
     * @param socialTokenDTO 第三方token信息
     * @return {@link SocialUserInfoDto} 第三方用户信息
     */
    public abstract SocialUserInfoDto getSocialUserInfo(SocialTokenDto socialTokenDTO);

    /**
     * 获取用户账号
     *
     * @return {@link UserAuth} 用户账号
     */
    private UserAuth getUserAuth(SocialTokenDto socialTokenDTO) {
        return userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getUsername, socialTokenDTO.getOpenId())
                .eq(UserAuth::getLoginType, socialTokenDTO.getLoginType()));
    }

    /**
     * 获取用户信息
     *
     * @param user      用户账号
     * @param ipAddress ip地址
     * @param ipSource  ip源
     * @return {@link UserDetailDto} 用户信息
     */
    private UserDetailDto getUserDetail(UserAuth user, String ipAddress, String ipSource) {
        // 更新登录信息
        userAuthMapper.update(new UserAuth(), new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getLastLoginTime, LocalDateTime.now())
                .set(UserAuth::getIpAddress, ipAddress)
                .set(UserAuth::getIpSource, ipSource)
                .eq(UserAuth::getId, user.getId()));
        // 封装信息
        return userDetailsService.convertUserDetail(user, request);
    }

    /**
     * 新增用户信息
     *
     * @param socialToken token信息
     * @param ipAddress   ip地址
     * @param ipSource    ip源
     * @return {@link UserDetailDto} 用户信息
     */
    private UserDetailDto saveUserDetail(SocialTokenDto socialToken, String ipAddress, String ipSource) {
        // 获取第三方用户信息
        SocialUserInfoDto socialUserInfo = getSocialUserInfo(socialToken);
        // 保存用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(socialUserInfo.getNickname())
                .avatar(socialUserInfo.getAvatar())
                .build();
        userInfoMapper.insert(userInfo);
        // 保存账号信息
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(socialToken.getOpenId())
                .password(socialToken.getAccessToken())
                .loginType(socialToken.getLoginType())
                .lastLoginTime(LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())))
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .build();
        userAuthMapper.insert(userAuth);
        // 绑定角色
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
        return userDetailsService.convertUserDetail(userAuth, request);
    }

}
