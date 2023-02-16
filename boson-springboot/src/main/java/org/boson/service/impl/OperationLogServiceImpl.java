package org.boson.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.PageResult;
import org.boson.domain.dto.OperationLogDTO;
import org.boson.domain.po.OperationLog;
import org.boson.domain.vo.ConditionVO;
import org.boson.mapper.OperationLogMapper;
import org.boson.service.OperationLogService;
import org.boson.support.mybatisplus.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public PageResult<OperationLogDTO> pageOperationLogs(ConditionVO conditionVO) {
        return this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptModule, conditionVO.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptDesc, conditionVO.getKeywords())
                .orderByDesc(OperationLog::getId)
                .queryPageAndPo2Vo(OperationLogDTO.class);
    }
}
