package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * 资源VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "资源")
public class ResourceVo {

    /**
     * 资源id
     */
    @ApiModelProperty(name = "id", value = "资源id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 父资源id
     */
    @ApiModelProperty(name = "pid", value = "父资源id", required = true, dataType = "Integer")
    private Integer pid;

    /**
     * 资源名
     */
    @NotBlank(message = "资源名不能为空")
    @ApiModelProperty(name = "resourceName", value = "资源名", required = true, dataType = "String")
    private String resourceName;

    /**
     * 资源路径
     */
    @ApiModelProperty(name = "url", value = "资源路径", required = true, dataType = "String")
    private String url;

    /**
     * 请求方式
     */
    @ApiModelProperty(name = "url", value = "资源路径", required = true, dataType = "String")
    private String requestMethod;

    /**
     * 是否匿名访问
     */
    @ApiModelProperty(name = "isAnonymous", value = "是否匿名访问", required = true, dataType = "Integer")
    private Integer isAnonymous;
}
