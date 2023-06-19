package org.boson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.boson.domain.po.ChatRecord;
import org.springframework.stereotype.Repository;


/**
 * 聊天记录表Mapper
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Repository
public interface ChatRecordMapper extends BaseMapper<ChatRecord> {
}
