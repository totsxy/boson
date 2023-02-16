package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.OperationLogDTO;
import org.boson.domain.po.OperationLog;
import org.boson.domain.vo.ConditionVO;
import org.boson.support.mybatisplus.service.Queryable;


/**
 * 操作日志服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface OperationLogService extends Queryable<OperationLog> {

    /**
     * 查询操作日志列表
     *
     * @param conditionVO 查询条件
     * @return 操作日志列表
     */
    PageResult<OperationLogDTO> pageOperationLogs(ConditionVO conditionVO);
}
