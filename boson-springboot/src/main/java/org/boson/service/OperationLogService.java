package org.boson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.boson.domain.PageResult;
import org.boson.domain.dto.OperationLogDTO;
import org.boson.domain.po.OperationLog;
import org.boson.domain.vo.ConditionVO;


/**
 * 操作日志服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 查询日志列表
     *
     * @param conditionVO 条件
     * @return 日志列表
     */
    PageResult<OperationLogDTO> queryPage(ConditionVO conditionVO);

}
