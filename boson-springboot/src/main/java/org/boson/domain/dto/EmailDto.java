package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 邮件DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    /**
     * 目标邮箱号
     */
    private String to;

    /**
     * 主题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

}
