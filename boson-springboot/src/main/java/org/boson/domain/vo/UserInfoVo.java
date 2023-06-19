package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


/**
 * 用户信息VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用户信息")
public class UserInfoVo {

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "用户昵称", dataType = "String")
    private String nickname;

    /**
     * 用户简介
     */
    @ApiModelProperty(name = "intro", value = "用户简介", dataType = "String")
    private String intro;

    /**
     * 个人网站
     */
    @ApiModelProperty(name = "webSite", value = "个人网站", dataType = "String")
    private String webSite;
}
