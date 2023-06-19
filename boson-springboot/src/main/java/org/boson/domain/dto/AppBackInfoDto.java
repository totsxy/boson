package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 博客后台信息DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppBackInfoDto {

    /**
     * 访问量
     */
    private Integer viewCount;

    /**
     * 用户量
     */
    private Integer userCount;

    /**
     * 一周用户量集合
     */
    private List<UniqueViewDto> uniqueViewDtoList;
}
