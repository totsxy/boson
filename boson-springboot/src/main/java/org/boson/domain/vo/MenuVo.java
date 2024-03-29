package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 菜单VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "菜单")
public class MenuVo {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "菜单id", dataType = "Integer")
    private Integer id;

    /**
     * 父菜单id
     */
    @ApiModelProperty(name = "parentId", value = "父菜单id", dataType = "Integer")
    private Integer parentId;

    /**
     * 菜单名
     */
    @NotBlank(message = "菜单名不能为空")
    @ApiModelProperty(name = "name", value = "菜单名", dataType = "String")
    private String name;

    /**
     * 菜单图标
     */
    @NotBlank(message = "菜单图标不能为空")
    @ApiModelProperty(name = "icon", value = "菜单图标", dataType = "String")
    private String icon;

    /**
     * 菜单路径
     */
    @NotBlank(message = "菜单路径不能为空")
    @ApiModelProperty(name = "path", value = "菜单路径", dataType = "String")
    private String path;

    /**
     * 菜单组件
     */
    @NotBlank(message = "菜单组件不能为空")
    @ApiModelProperty(name = "component", value = "菜单组件", dataType = "String")
    private String component;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(name = "isHidden", value = "是否隐藏", dataType = "Integer")
    private Integer isHidden;

    /**
     * 菜单排序
     */
    @NotNull(message = "菜单排序不能为空")
    @ApiModelProperty(name = "orderNum", value = "菜单排序", dataType = "Integer")
    private Integer orderNum;
}
