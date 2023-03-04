package org.boson.domain;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 分页对象
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "分页对象")
public class PageResult<T> {

    /**
     * 分页列表
     */
    @ApiModelProperty(name = "recordList", value = "分页列表", required = true, dataType = "List<T>")
    private List<T> recordList;

    /**
     * 总数
     */
    @ApiModelProperty(name = "count", value = "总数", required = true, dataType = "Integer")
    private Integer count;


    public static <R> PageResult<R> of(List<R> record, Number total) {
        return new PageResult<>(record, Convert.toInt(total));
    }

    public static <R> PageResult<R> of(IPage<R> page) {
        return of(page.getRecords(), page.getTotal());
    }
}
