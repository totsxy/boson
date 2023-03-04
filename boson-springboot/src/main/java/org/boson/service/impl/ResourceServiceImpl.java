package org.boson.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.dto.LabelOptionDto;
import org.boson.domain.dto.ResourceDto;
import org.boson.domain.po.Resource;
import org.boson.domain.po.RoleResource;
import org.boson.domain.vo.ConditionVo;
import org.boson.exception.BizException;
import org.boson.handler.ResourceRoleMetadataSourceImpl;
import org.boson.domain.vo.ResourceVo;
import org.boson.mapper.ResourceMapper;
import org.boson.service.ResourceService;
import org.boson.service.RoleResourceService;
import org.boson.support.mybatisplus.service.BaseServiceImpl;
import org.boson.util.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.boson.constant.CommonConstants.FALSE;

/**
 * 资源服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final RoleResourceService roleResourceService;

    private final ResourceRoleMetadataSourceImpl resourceRoleMetadataSource;

    public ResourceServiceImpl(RoleResourceService roleResourceService, ResourceRoleMetadataSourceImpl resourceRoleMetadataSource) {
        this.roleResourceService = roleResourceService;
        this.resourceRoleMetadataSource = resourceRoleMetadataSource;
    }

    @Override
    public boolean saveOrUpdateResource(ResourceVo resourceVo) {
        Resource resource = BeanUtils.bean2Bean(resourceVo, Resource.class);
        boolean savedOrUpdated = this.saveOrUpdate(resource);
        if (savedOrUpdated) {
            this.resourceRoleMetadataSource.clearDataSource();
        }
        return savedOrUpdated;
    }

    @Override
    public boolean deleteResource(Integer resourceId) {
        // 查询是否有角色关联
        int count = this.roleResourceService.beginQuery()
                .eq(RoleResource::getResourceId, resourceId)
                .count();

        if (count > 0) {
            throw new BizException("该资源下存在角色");
        }

        // 删除子资源
        List<Integer> resourceIdList = this.beginQuery()
                .select(Resource::getId)
                .eq(Resource::getPid, resourceId)
                .queryList()
                .stream()
                .map(Resource::getId)
                .collect(Collectors.toList());

        resourceIdList.add(resourceId);

        boolean deleted = resourceIdList.size() == getBaseMapper().deleteBatchIds(resourceIdList);
        if (deleted) {
            this.resourceRoleMetadataSource.clearDataSource();
        }
        return deleted;
    }

    @Override
    public List<ResourceDto> listResources(ConditionVo conditionVo) {
        List<Resource> resourceList = this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Resource::getResourceName, conditionVo.getKeywords())
                .queryList();
        Map<Integer, List<Resource>> childrenResourceMap = this.listChildrenResource(resourceList);

        List<ResourceDto> resourceDtoList = this.listModule(resourceList).stream()
                .map(module -> {
                    ResourceDto resourceDto = BeanUtils.bean2Bean(module, ResourceDto.class);
                    List<ResourceDto> children = BeanUtils.bean2Bean(childrenResourceMap.remove(module.getId()), ResourceDto.class);
                    resourceDto.setChildren(children);
                    return resourceDto;
                }).collect(Collectors.toList());

        // 若还有资源未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenResourceMap)) {
            List<Resource> childrenResourceList = new ArrayList<>();
            childrenResourceMap.values().forEach(childrenResourceList::addAll);

            List<ResourceDto> childrenResourceDtoList = childrenResourceList.stream()
                    .map(resource -> BeanUtils.bean2Bean(resource, ResourceDto.class))
                    .collect(Collectors.toList());
            resourceDtoList.addAll(childrenResourceDtoList);
        }
        return resourceDtoList;
    }

    @Override
    public List<LabelOptionDto> listResourceOption() {
        List<Resource> resourceList = this.beginQuery()
                .select(Resource::getId, Resource::getResourceName, Resource::getPid)
                .eq(Resource::getIsAnonymous, FALSE)
                .queryList();
        Map<Integer, List<Resource>> childrenResourceMap = listChildrenResource(resourceList);

        return this.listModule(resourceList).stream()
                .map(module -> {
                    List<LabelOptionDto> children;
                    List<Resource> childrenResourceList = childrenResourceMap.get(module.getId());

                    if (CollectionUtil.isEmpty(childrenResourceList)) {
                        children = new ArrayList<>(0);
                    } else {
                        children = childrenResourceList.stream()
                                .map(resource -> LabelOptionDto.builder()
                                        .id(resource.getId())
                                        .label(resource.getResourceName())
                                        .build())
                                .collect(Collectors.toList());
                    }

                    return LabelOptionDto.builder()
                            .id(module.getId())
                            .label(module.getResourceName())
                            .children(children)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<Resource> listModule(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(resource -> Objects.isNull(resource.getPid()))
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Resource>> listChildrenResource(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(resource -> Objects.nonNull(resource.getPid()))
                .collect(Collectors.groupingBy(Resource::getPid));
    }
}
