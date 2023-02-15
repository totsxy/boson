package org.boson.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.boson.domain.PageResult;
import org.boson.domain.Result;
import org.boson.domain.dto.OperationLogDTO;
import org.boson.domain.vo.ConditionVO;
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

    private final OperationLogService operationLogService;

    @Autowired
    public LogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 查看操作日志
     *
     * @param conditionVO 条件
     * @return {@link Result<OperationLogDTO>} 日志列表
     */
    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/operation/logs")
    public Result<PageResult<OperationLogDTO>> listOperationLogs(ConditionVO conditionVO) {
        return Result.ok(operationLogService.queryPage(conditionVO));
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
        operationLogService.removeByIds(logIdList);
        return Result.ok();
    }
}
