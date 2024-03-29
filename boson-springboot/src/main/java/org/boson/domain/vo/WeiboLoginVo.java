package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * 微博登录信息VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "微博登录信息")
public class WeiboLoginVo {

    /**
     * code
     */
    @NotBlank(message = "code不能为空")
    @ApiModelProperty(name = "openId", value = "微博 openId", required = true, dataType = "String")
    private String code;
}
