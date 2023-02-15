package org.boson.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.PageResult;
import org.boson.domain.dto.OperationLogDTO;
import org.boson.domain.po.OperationLog;
import org.boson.domain.vo.ConditionVO;
import org.boson.mapper.OperationLogMapper;
import org.boson.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * 操作日志服务
 *
 * @author yezhiqiu
 * @date 2021/08/08
 */
@Service
public class OperationLogServiceImpl extends BaseServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public PageResult<OperationLogDTO> queryPage(ConditionVO conditionVO) {
//        Page<OperationLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
//        // 查询日志列表
//        Page<OperationLog> operationLogPage = this.page(page, new LambdaQueryWrapper<OperationLog>()
//                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptModule, conditionVO.getKeywords())
//                .or()
//                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptDesc, conditionVO.getKeywords())
//                .orderByDesc(OperationLog::getId));
//        List<OperationLogDTO> operationLogDTOList = BeanCopyUtils.copyList(operationLogPage.getRecords(), OperationLogDTO.class);
//        return new PageResult<>(operationLogDTOList, (int) operationLogPage.getTotal());

        return this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptModule, conditionVO.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), OperationLog::getOptDesc, conditionVO.getKeywords())
                .orderByDesc(OperationLog::getId)
                .queryPageAndPo2Vo(OperationLogDTO.class);
    }

}
