package org.boson.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.boson.annotation.LogOperation;
import org.boson.domain.Result;
import org.boson.domain.dto.AppBackInfoDto;
import org.boson.domain.dto.AppHomeInfoDto;
import org.boson.domain.vo.AppInfoVo;
import org.boson.domain.vo.AppConfigVo;
import org.boson.enums.OperationEnum;
import org.boson.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * 应用信息控制器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Api(tags = "应用信息模块")
@RestController
public class AppInfoController {

    @Autowired
    private AppInfoService appInfoService;

    /**
     * 查看前台信息
     *
     * @return {@link Result<AppHomeInfoDto>} 前台信息
     */
    @ApiOperation(value = "查看前台信息")
    @GetMapping("/")
    public Result<AppHomeInfoDto> getAppHomeInfo() {
        return Result.ok(appInfoService.getAppHomeInfo());
    }

    /**
     * 查看后台信息
     *
     * @return {@link Result<AppBackInfoDto>} 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result<AppBackInfoDto> getAppBackInfo() {
        return Result.ok(appInfoService.getAppBackInfo());
    }

    /**
     * 更新应用配置
     *
     * @param appConfigVo 应用配置信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "更新应用配置")
    @PutMapping("/admin/website/config")
    public Result<?> saveOrUpdateAppConfig(@Valid @RequestBody AppConfigVo appConfigVo) {
        return Result.check(appInfoService.saveOrUpdateAppConfig(appConfigVo));
    }

    /**
     * 查看应用配置
     *
     * @return {@link Result<AppConfigVo>} 应用配置
     */
    @ApiOperation(value = "查看应用配置")
    @GetMapping("/admin/website/config")
    public Result<AppConfigVo> getAppConfig() {
        return Result.ok(appInfoService.getAppConfig());
    }

    /**
     * 修改关于我信息
     *
     * @param appInfoVo 博客信息
     * @return {@link Result<>}
     */
    @LogOperation(OperationEnum.Update)
    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/admin/about")
    public Result<?> updateAbout(@Valid @RequestBody AppInfoVo appInfoVo) {
        return Result.check(appInfoService.updateAbout(appInfoVo));
    }

    /**
     * 查看关于我信息
     *
     * @return {@link Result<String>} 关于我信息
     */
    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    public Result<String> getAbout() {
        return Result.ok(appInfoService.getAbout());
    }

    /**
     * 上传访客信息
     *
     * @return {@link Result<>}
     */
    @PostMapping("/report")
    public Result<?> report() {
        appInfoService.report();
        return Result.ok();
    }
}
