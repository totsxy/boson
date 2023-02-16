package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.PhotoAlbumBackDto;
import org.boson.domain.dto.PhotoAlbumDto;
import org.boson.domain.po.PhotoAlbum;
import com.baomidou.mybatisplus.extension.service.IService;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.PhotoAlbumVo;

import java.util.List;

/**
 * 相册服务
 *
 * @author yezhiqiu
 * @date 2021/08/04
 */
public interface PhotoAlbumService extends IService<PhotoAlbum> {

    /**
     * 保存或更新相册
     *
     * @param photoAlbumVO 相册信息
     */
    void saveOrUpdatePhotoAlbum(PhotoAlbumVo photoAlbumVO);

    /**
     * 查看后台相册列表
     *
     * @param condition 条件
     * @return {@link PageResult<  PhotoAlbumBackDto  >} 相册列表
     */
    PageResult<PhotoAlbumBackDto> listPhotoAlbumBacks(ConditionVo condition);

    /**
     * 获取后台相册列表信息
     *
     * @return {@link List< PhotoAlbumDto >} 相册列表信息
     */
    List<PhotoAlbumDto> listPhotoAlbumBackInfos();

    /**
     * 根据id获取相册信息
     *
     * @param albumId 相册id
     * @return {@link PhotoAlbumBackDto} 相册信息
     */
    PhotoAlbumBackDto getPhotoAlbumBackById(Integer albumId);

    /**
     * 根据id删除相册
     *
     * @param albumId 相册id
     */
    void deletePhotoAlbumById(Integer albumId);

    /**
     * 获取相册列表
     *
     * @return {@link List< PhotoAlbumDto >}相册列表
     */
    List<PhotoAlbumDto> listPhotoAlbums();

}
