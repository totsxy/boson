package org.boson.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.PageResult;
import org.boson.domain.dto.OperationLogDto;
import org.boson.domain.po.OperationLog;
import org.boson.domain.vo.ConditionVo;
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
    public PageResult<OperationLogDto> pageOperationLogs(ConditionVo conditionVo) {
        return this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), OperationLog::getOptModule, conditionVo.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), OperationLog::getOptDesc, conditionVo.getKeywords())
                .orderByDesc(OperationLog::getId)
                .queryPageAndPo2Vo(OperationLogDto.class);
    }
}
