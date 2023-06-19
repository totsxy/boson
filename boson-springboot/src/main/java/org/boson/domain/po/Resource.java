package org.boson.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.boson.domain.Entity;


/**
 * 资源PO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@TableName("tb_resource")
public class Resource extends Entity {

    /**
     * 父资源id
     */
    private Integer pid;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 资源路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 是否匿名访问
     */
    private Integer isAnonymous;
}
