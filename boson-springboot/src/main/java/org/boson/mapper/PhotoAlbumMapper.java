package org.boson.mapper;

import org.boson.domain.dto.PhotoAlbumBackDTO;
import org.boson.domain.po.PhotoAlbum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.boson.domain.vo.ConditionVO;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 相册映射器
 *
 * @author yezhiqiu
 * @date 2021/08/04
 */
@Repository
public interface PhotoAlbumMapper extends BaseMapper<PhotoAlbum> {

    /**
     * 查询后台相册列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List < PhotoAlbumBackDTO >} 相册列表
     */
    List<PhotoAlbumBackDTO> listPhotoAlbumBacks(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}




