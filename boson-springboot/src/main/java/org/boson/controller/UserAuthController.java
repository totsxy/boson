package org.boson.controller;

import org.boson.annotation.AccessLimit;
import org.boson.domain.PageResult;
import org.boson.domain.Result;
import org.boson.domain.dto.UserAreaDto;
import org.boson.domain.dto.UserBackDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.vo.*;
import org.boson.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户账号控制器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Api(tags = "用户账号模块")
@RestController
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Autowired
    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    /**
     * 发送邮箱验证码
     *
     * @param username 用户名
     * @return {@link Result<>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String")
    @GetMapping("/users/code")
    public Result<?> sendCode(String username) {
        this.userAuthService.sendCode(username);
        return Result.ok();
    }

    /**
     * 用户注册
     *
     * @param userVo 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody UserVo userVo) {
        return Result.check(this.userAuthService.register(userVo));
    }

    /**
     * 修改密码
     *
     * @param userVo 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    public Result<?> updatePassword(@Valid @RequestBody UserVo userVo) {
        return Result.check(this.userAuthService.updatePassword(userVo));
    }

    /**
     * 修改管理员密码
     *
     * @param passwordVo 密码信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    public Result<?> updateAdminPassword(@Valid @RequestBody PasswordVo passwordVo) {
        return Result.check(this.userAuthService.updateAdminPassword(passwordVo));
    }

    /**
     * qq登录
     *
     * @param qqLoginVo qq登录信息
     * @return {@link Result<UserInfoDto>} 用户信息
     */
    @ApiOperation(value = "qq登录")
    @PostMapping("/users/oauth/qq")
    public Result<UserInfoDto> qqLogin(@Valid @RequestBody QQLoginVo qqLoginVo) {
        return Result.ok(this.userAuthService.qqLogin(qqLoginVo));
    }

    /**
     * 微博登录
     *
     * @param weiboLoginVo 微博登录信息
     * @return {@link Result<UserInfoDto>} 用户信息
     */
    @ApiOperation(value = "微博登录")
    @PostMapping("/users/oauth/weibo")
    public Result<UserInfoDto> weiboLogin(@Valid @RequestBody WeiboLoginVo weiboLoginVo) {
        return Result.ok(this.userAuthService.weiboLogin(weiboLoginVo));
    }

    /**
     * 分页查询后台用户列表
     *
     * @param conditionVo 查询条件
     * @return {@link Result<UserBackDto>} 后台用户列表
     */
    @ApiOperation(value = "分页查询后台用户列表")
    @GetMapping("/admin/users")
    public Result<PageResult<UserBackDto>> listUsers(ConditionVo conditionVo) {
        return Result.ok(this.userAuthService.pageUserBacks(conditionVo));
    }

    /**
     * 获取用户区域分布
     *
     * @param conditionVo 查询条件
     * @return {@link Result<UserAreaDto>} 用户区域分布
     */
    @ApiOperation(value = "获取用户区域分布")
    @GetMapping("/admin/users/area")
    public Result<List<UserAreaDto>> listUserAreas(ConditionVo conditionVo) {
        return Result.ok(this.userAuthService.listUserAreas(conditionVo));
    }
}
