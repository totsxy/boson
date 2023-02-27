package org.boson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.boson.domain.po.ChatRecord;
import org.boson.domain.po.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRecordMapper extends BaseMapper<ChatRecord>  {
}
