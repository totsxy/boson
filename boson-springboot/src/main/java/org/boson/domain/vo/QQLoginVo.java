package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * QQ登录信息VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "QQ登录信息")
public class QQLoginVo {

    /**
     * openId
     */
    @NotBlank(message = "openId不能为空")
    @ApiModelProperty(name = "openId", value = "QQ openId", required = true, dataType = "String")
    private String openId;

    /**
     * accessToken
     */
    @NotBlank(message = "accessToken不能为空")
    @ApiModelProperty(name = "accessToken", value = "QQ accessToken", required = true, dataType = "String")
    private String accessToken;
}
