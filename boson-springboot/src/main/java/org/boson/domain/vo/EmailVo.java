package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


/**
 * 邮箱VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "邮箱")
public class EmailVo {

    /**
     * 邮箱账户
     */
    @NotBlank(message = "邮箱账户不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(name = "email", value = "邮箱账户", required = true, dataType = "String")
    private String email;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(name = "code", value = "验证码", required = true, dataType = "String")
    private String code;
}
