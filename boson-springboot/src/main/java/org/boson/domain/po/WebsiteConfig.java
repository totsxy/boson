package org.boson.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.boson.domain.Entity;

import java.time.LocalDateTime;

/**
 * 网站配置PO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@TableName(value = "tb_website_config")
public class WebsiteConfig extends Entity {

    /**
     * 配置信息
     */
    private String config;
}
