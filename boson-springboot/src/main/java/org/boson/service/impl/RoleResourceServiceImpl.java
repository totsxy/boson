package org.boson.service.impl;

import org.boson.domain.po.RoleResource;
import org.boson.mapper.RoleResourceMapper;
import org.boson.service.RoleResourceService;
import org.boson.support.service.BaseServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 角色资源服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class RoleResourceServiceImpl extends BaseServiceImpl<RoleResourceMapper, RoleResource> implements RoleResourceService {
}
