package org.boson.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 应用信息VO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "应用信息")
public class AppInfoVo {

    /**
     * 关于我内容
     */
    @ApiModelProperty(name = "aboutContent", value = "关于我内容", required = true, dataType = "String")
    private String aboutContent;
}
