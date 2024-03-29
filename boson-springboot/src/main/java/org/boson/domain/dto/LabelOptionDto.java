package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 标签选项
 *
 * @author ShenXiaoYu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LabelOptionDto {

    /**
     * 选项id
     */
    private Integer id;

    /**
     * 选项名
     */
    private String label;

    /**
     * 子选项
     */
    private List<LabelOptionDto> children;
}
