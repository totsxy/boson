package org.boson.service;

import org.boson.domain.dto.LabelOptionDto;
import org.boson.domain.dto.ResourceDto;
import org.boson.domain.po.Resource;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.ResourceVo;
import org.boson.support.service.BaseService;

import java.util.List;


/**
 * 资源服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface ResourceService extends BaseService<Resource> {

    /**
     * 保存或修改资源
     *
     * @param resourceVo 资源信息
     */
    boolean saveOrUpdateResource(ResourceVo resourceVo);

    /***
     * 删除资源
     *
     @param resourceId 资源id
     */
    boolean deleteResource(Integer resourceId);

    /**
     * 查询资源列表
     *
     * @param conditionVo 查询条件
     * @return 资源列表
     */
    List<ResourceDto> listResources(ConditionVo conditionVo);

    /**
     * 查看角色资源选项
     *
     * @return 角色资源选项列表
     */
    List<LabelOptionDto> listResourceOption();
}
