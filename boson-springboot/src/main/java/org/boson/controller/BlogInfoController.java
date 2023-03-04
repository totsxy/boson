package org.boson.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.boson.annotation.LogOperation;
import org.boson.domain.Result;
import org.boson.domain.dto.BlogBackInfoDto;
import org.boson.domain.dto.BlogHomeInfoDto;
import org.boson.domain.vo.BlogInfoVo;
import org.boson.domain.vo.VoiceVo;
import org.boson.domain.vo.WebsiteConfigVo;
import org.boson.enums.FilePathEnum;
import org.boson.enums.OperationEnum;
import org.boson.service.BlogInfoService;
import org.boson.service.impl.WebSocketServiceImpl;
import org.boson.strategy.context.UploadStrategyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


/**
 * 博客信息控制器
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Api(tags = "博客信息模块")
@RestController
public class BlogInfoController {
    @Autowired
    private BlogInfoService blogInfoService;
    @Autowired
    private WebSocketServiceImpl webSocketService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    /**
     * 查看博客信息
     *
     * @return {@link Result<BlogHomeInfoDto>} 博客信息
     */
    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    public Result<BlogHomeInfoDto> getBlogHomeInfo() {
        return Result.ok(blogInfoService.getBlogHomeInfo());
    }

    /**
     * 查看后台信息
     *
     * @return {@link Result<BlogBackInfoDto>} 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result<BlogBackInfoDto> getBlogBackInfo() {
        return Result.ok(blogInfoService.getBlogBackInfo());
    }

    /**
     * 上传博客配置图片
     *
     * @param file 文件
     * @return {@link Result<String>} 博客配置图片
     */
    @ApiOperation(value = "上传博客配置图片")
    @ApiImplicitParam(name = "file", value = "图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/config/images")
    public Result<String> savePhotoAlbumCover(MultipartFile file) {
        return Result.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.CONFIG.getPath()));
    }

    /**
     * 更新网站配置
     *
     * @param websiteConfigVo 网站配置信息
     * @return {@link Result}
     */
    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/website/config")
    public Result<?> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVo websiteConfigVo) {
        blogInfoService.updateWebsiteConfig(websiteConfigVo);
        return Result.ok();
    }

    /**
     * 获取网站配置
     *
     * @return {@link Result<WebsiteConfigVo>} 网站配置
     */
    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    public Result<WebsiteConfigVo> getWebsiteConfig() {
        return Result.ok(blogInfoService.getWebsiteConfig());
    }

    /**
     * 查看关于我信息
     *
     * @return {@link Result<String>} 关于我信息
     */
    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    public Result<String> getAbout() {
        return Result.ok(blogInfoService.getAbout());
    }

    /**
     * 修改关于我信息
     *
     * @param blogInfoVo 博客信息
     * @return {@link Result<>}
     */
    @LogOperation(OperationEnum.Update)
    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/admin/about")
    public Result<?> updateAbout(@Valid @RequestBody BlogInfoVo blogInfoVo) {
        blogInfoService.updateAbout(blogInfoVo);
        return Result.ok();
    }

    /**
     * 保存语音信息
     *
     * @param voiceVo 语音信息
     * @return {@link Result<String>} 语音地址
     */
    @ApiOperation(value = "上传语音")
    @PostMapping("/voice")
    public Result<String> sendVoice(VoiceVo voiceVo) {
        webSocketService.sendVoice(voiceVo);
        return Result.ok();
    }

    /**
     * 上传访客信息
     *
     * @return {@link Result}
     */
    @PostMapping("/report")
    public Result<?> report() {
        blogInfoService.report();
        return Result.ok();
    }

}

