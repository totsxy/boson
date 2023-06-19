package org.boson.handler;

import org.boson.domain.dto.ResourceRoleDto;
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
public class SecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    /**
     * 资源角色列表
     */
    private static List<ResourceRoleDto> RESOURCE_ROLE_LIST;

    @Autowired
    private RoleService roleService;

    /**
     * 加载资源角色信息
     */
    public boolean loadResourceRole(boolean reload) {
        if (reload) {
            RESOURCE_ROLE_LIST = this.roleService.listResourceRoles();
        }
        return reload;
    }

    @PostConstruct
    public void loadResourceRole() {
        this.loadResourceRole(true);
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation invocation = (FilterInvocation) object;
        String method = invocation.getRequest().getMethod();
        String url = invocation.getRequest().getRequestURI();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        final List<ResourceRoleDto> resourceRoleDtoList = RESOURCE_ROLE_LIST;

        // 获取接口角色信息，若为匿名接口则放行，若无对应角色则禁止
        for (ResourceRoleDto resourceRoleDto : resourceRoleDtoList) {
            if (antPathMatcher.match(resourceRoleDto.getUrl(), url) && resourceRoleDto.getRequestMethod().equals(method)) {
                List<String> roleList = resourceRoleDto.getRoleList();
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
