package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.OperationLogDto;
import org.boson.domain.po.OperationLog;
import org.boson.domain.vo.ConditionVo;
import org.boson.support.service.BaseService;


/**
 * 操作日志服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface OperationLogService extends BaseService<OperationLog> {

    /**
     * 查询操作日志列表
     *
     * @param conditionVo 查询条件
     * @return 操作日志列表
     */
    PageResult<OperationLogDto> pageOperationLogs(ConditionVo conditionVo);
}
