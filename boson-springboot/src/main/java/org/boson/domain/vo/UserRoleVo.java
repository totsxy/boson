package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * 用户角色VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "用户权限")
public class UserRoleVo {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(name = "userInfoId", value = "用户信息id", dataType = "Integer")
    private Integer userInfoId;

    /**
     * 用户昵称
     */
    @NotBlank(message = "用户昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "用户昵称", dataType = "String")
    private String nickname;

    /**
     * 用户角色id列表
     */
    @NotNull(message = "用户角色id列表不能为空")
    @ApiModelProperty(name = "roleList", value = "用户角色id列表", dataType = "List<Integer>")
    private List<Integer> roleIdList;
}
