package org.boson.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * cos配置属性
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "upload.cos")
public class CosConfigProperties {

    private String url;

    private String secretId;

    private String secretKey;

    private String region;

    private String bucketName;
}
