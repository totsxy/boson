package org.boson.controller;

import org.boson.annotation.OperationLog;
import org.boson.domain.PageResult;
import org.boson.domain.Result;
import org.boson.domain.dto.UserOnlineDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.boson.domain.vo.*;
import org.boson.enums.OperationEnum;
import org.boson.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


/**
 * 用户信息控制器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Api(tags = "用户信息模块")
@RestController
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 更新用户信息
     *
     * @param userInfoVo 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "更新用户信息")
    @PutMapping("/users/info")
    public Result<?> updateUserInfo(@Valid @RequestBody UserInfoVo userInfoVo) {
        return Result.check(userInfoService.updateUserInfo(userInfoVo));
    }

    /**
     * 更新用户头像
     *
     * @param file 文件
     * @return {@link Result<String>} 头像地址
     */
    @ApiOperation(value = "更新用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile")
    @PostMapping("/users/avatar")
    public Result<String> updateUserAvatar(MultipartFile file) {
        return Result.check(userInfoService.updateUserAvatar(file));
    }

    /**
     * 绑定用户邮箱
     *
     * @param emailVo 邮箱信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "绑定用户邮箱")
    @PostMapping("/users/email")
    public Result<?> saveUserEmail(@Valid @RequestBody EmailVo emailVo) {
        return Result.check(userInfoService.saveUserEmail(emailVo));
    }

    /**
     * 修改用户角色
     *
     * @param userRoleVo 用户角色信息
     * @return {@link Result<>}
     */
    @OperationLog(OperationEnum.Update)
    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/users/role")
    public Result<?> updateUserRole(@Valid @RequestBody UserRoleVo userRoleVo) {
        return Result.check(userInfoService.updateUserRole(userRoleVo));
    }

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVo 用户禁用信息
     * @return {@link Result<>}
     */
    @OperationLog(OperationEnum.Update)
    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable")
    public Result<?> updateUserDisable(@Valid @RequestBody UserDisableVo userDisableVo) {
        return Result.check(userInfoService.updateUserDisable(userDisableVo));
    }

    /**
     * 查看在线用户
     *
     * @param conditionVo 查询条件
     * @return {@link Result<UserOnlineDto>} 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @GetMapping("/admin/users/online")
    public Result<PageResult<UserOnlineDto>> listOnlineUsers(ConditionVo conditionVo) {
        return Result.ok(userInfoService.listOnlineUsers(conditionVo));
    }

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "下线用户")
    @DeleteMapping("/admin/users/{userInfoId}/online")
    public Result<?> removeOnlineUser(@PathVariable("userInfoId") Integer userInfoId) {
        userInfoService.removeOnlineUser(userInfoId);
        return Result.ok();
    }
}
