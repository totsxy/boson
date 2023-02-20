package org.boson.controller;

import org.boson.domain.Result;
import org.boson.domain.dto.LabelOptionDto;
import org.boson.domain.dto.ResourceDto;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.ResourceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.boson.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 资源控制器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Api(tags = "资源模块")
@RestController
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * 新增或修改资源
     *
     * @param resourceVo 资源信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/admin/resources")
    public Result<?> saveOrUpdateResource(@RequestBody @Valid ResourceVo resourceVo) {
        return Result.check(this.resourceService.saveOrUpdateResource(resourceVo));
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除资源")
    @DeleteMapping("/admin/resources/{resourceId}")
    public Result<?> deleteResource(@PathVariable("resourceId") Integer resourceId) {
        return Result.check(this.resourceService.deleteResource(resourceId));
    }

    /**
     * 查看资源列表
     *
     * @return {@link Result<ResourceDto>} 资源列表
     */
    @ApiOperation(value = "查看资源列表")
    @GetMapping("/admin/resources")
    public Result<List<ResourceDto>> listResources(ConditionVo conditionVo) {
        return Result.ok(this.resourceService.listResources(conditionVo));
    }

    /**
     * 查看角色资源选项
     *
     * @return {@link Result<LabelOptionDto>} 角色资源选项列表
     */
    @ApiOperation(value = "查看角色资源选项")
    @GetMapping("/admin/role/resources")
    public Result<List<LabelOptionDto>> listResourceOption() {
        return Result.ok(this.resourceService.listResourceOption());
    }
}
