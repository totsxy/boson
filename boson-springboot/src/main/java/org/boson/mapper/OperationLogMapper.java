package org.boson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.boson.domain.po.OperationLog;
import org.springframework.stereotype.Repository;


/**
 * 操作日志表Mapper
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Repository
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
