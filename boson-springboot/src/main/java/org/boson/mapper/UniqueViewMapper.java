package org.boson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.boson.domain.dto.UniqueViewDto;
import org.boson.domain.po.UniqueView;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 访问量
 *
 * @author xiaojie
 * @date 2021/08/10
 * @since 2020-05-18
 */
@Repository
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 获取7天用户量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户量
     */
    List<UniqueViewDto> listUniqueViews(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

}
