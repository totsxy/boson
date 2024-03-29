package org.boson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.boson.domain.dto.UniqueViewDto;
import org.boson.domain.po.UniqueView;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * 访问量统计表mapper
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Repository
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 查看7天用户量统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户量统计列表
     */
    List<UniqueViewDto> listUniqueViews(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
