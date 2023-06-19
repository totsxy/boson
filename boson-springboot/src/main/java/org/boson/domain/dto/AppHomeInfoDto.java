package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.boson.domain.vo.AppConfigVo;


/**
 * 应用前台信息
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppHomeInfoDto {

    /**
     * 访问量
     */
    private Integer viewCount;

    /**
     * 应用配置
     */
    private AppConfigVo appConfig;
}
