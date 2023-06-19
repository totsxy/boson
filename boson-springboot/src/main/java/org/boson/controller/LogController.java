package org.boson.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.boson.domain.PageResult;
import org.boson.domain.Result;
import org.boson.domain.dto.OperationLogDto;
import org.boson.domain.vo.ConditionVo;
import org.boson.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 日志控制器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Api(tags = "日志模块")
@RestController
public class LogController {

    @Autowired
    private  OperationLogService operationLogService;

    /**
     * 分页查询操作日志
     *
     * @param conditionVo 查询条件
     * @return {@link Result<OperationLogDto>} 日志列表
     */
    @ApiOperation(value = "分页查看操作日志")
    @GetMapping("/admin/operation/logs")
    public Result<PageResult<OperationLogDto>> pageOperationLogs(ConditionVo conditionVo) {
        return Result.ok(this.operationLogService.pageOperationLogs(conditionVo));
    }

    /**
     * 删除操作日志
     *
     * @param logIdList 日志id列表
     * @return {@link Result<>}
     */
    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/admin/operation/logs")
    public Result<?> deleteOperationLogs(@RequestBody List<Integer> logIdList) {
        return Result.check(this.operationLogService.removeByIds(logIdList));
    }
}
