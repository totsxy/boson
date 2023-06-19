package org.boson.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.po.UserAuth;
import org.boson.domain.dto.UserDetailDto;
import org.boson.domain.po.UserInfo;
import org.boson.exception.BizException;
import org.boson.service.RedisService;
import org.boson.service.RoleService;
import org.boson.service.UserAuthService;
import org.boson.service.UserInfoService;
import org.boson.util.IpUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.boson.enums.ZoneEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


/**
 * 用户详细信息服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserInfoService userInfoService;
    @Resource
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BizException("用户名不能为空");
        }

        // 查询账户是否存在
        UserAuth userAuth = this.userAuthService.beginQuery()
                .select(UserAuth::getId, UserAuth::getUserInfoId, UserAuth::getUsername, UserAuth::getPassword, UserAuth::getLoginType)
                .eq(UserAuth::getUsername, username)
                .getOne();

        if (Objects.isNull(userAuth)) {
            throw new BizException("用户账户不存在");
        }

        // 封装登录信息
        return this.convertUserDetail(userAuth, request);
    }

    /**
     * 封装用户登录信息
     *
     * @param user    用户账号
     * @param request 用户请求
     * @return 用户登录信息
     */
    public UserDetailDto convertUserDetail(UserAuth user, HttpServletRequest request) {
        // 查询账号及角色信息
        UserInfo userInfo = this.userInfoService.getById(user.getUserInfoId());
        List<String> roleList = this.roleService.listRolesByUserInfoId(userInfo.getId());

        // 获取设备信息
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);

        // 封装权限集合
        return UserDetailDto.builder()
                .id(user.getId())
                .loginType(user.getLoginType())
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(userInfo.getEmail())
                .roleList(roleList)
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .isDisable(userInfo.getIsDisable())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())))
                .build();
    }
}
