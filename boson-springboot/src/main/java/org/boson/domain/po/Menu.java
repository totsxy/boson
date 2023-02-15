package org.boson.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.boson.domain.Entity;


/**
 * 菜单PO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@TableName("tb_menu")
public class Menu extends Entity {

    /**
     * 父id
     */
    @TableField("pid")
    private Integer pid;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * icon
     */
    private String icon;

    /**
     * 组件
     */
    private String component;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 是否隐藏
     */
    private Integer isHidden;

}

