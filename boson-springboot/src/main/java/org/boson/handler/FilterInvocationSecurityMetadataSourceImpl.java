package org.boson.handler;

import org.boson.domain.dto.ResourceRoleDTO;
import org.boson.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

/**
 * 接口拦截规则
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    /**
     * 资源角色列表
     */
    private static List<ResourceRoleDTO> RESOURCE_ROLE_LIST;

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 加载资源角色信息
     */
    @PostConstruct
    private void loadDataSource() {
        RESOURCE_ROLE_LIST = this.roleService.listResourceRoles();
    }

    public void reloadDataSource() {
        if (CollectionUtils.isEmpty(RESOURCE_ROLE_LIST)) {
            this.loadDataSource();
        }
    }

    public void clearDataSource() {
        RESOURCE_ROLE_LIST = null;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 修改接口角色关系后重新加载
        this.reloadDataSource();

        FilterInvocation fi = (FilterInvocation) object;
        // 获取用户请求方式
        String method = fi.getRequest().getMethod();
        // 获取用户请求Url
        String url = fi.getRequest().getRequestURI();


//        HttpServletRequest request = fi.getRequest();
        AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//        List<String> roleList = resourceRoleList.stream()
//                .anyMatch(it->antPathMatcher.match(it.getUrl(), request.getRequestURI()) && it.getRequestMethod().equals(request.getMethod()))
//


        // 获取接口角色信息，若为匿名接口则放行，若无对应角色则禁止
        for (ResourceRoleDTO resourceRoleDTO : RESOURCE_ROLE_LIST) {
            if (antPathMatcher.match(resourceRoleDTO.getUrl(), url) && resourceRoleDTO.getRequestMethod().equals(method)) {
                List<String> roleList = resourceRoleDTO.getRoleList();
                if (CollectionUtils.isEmpty(roleList)) {
                    return SecurityConfig.createList("disable");
                }
                return SecurityConfig.createList(roleList.toArray(new String[]{}));
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
