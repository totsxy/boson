package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * 后台用户DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserBackDto {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户信息id
     */
    private Integer userInfoId;

    /**
     * 登录类型
     */
    private Integer loginType;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否禁用
     */
    private Integer isDisable;

    /**
     * 用户登录ip
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户角色
     */
    private List<UserRoleDto> roleList;
}
