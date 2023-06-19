package org.boson.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * qq配置属性
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfigProperties {

    /**
     * QQ appId
     */
    private String appId;

    /**
     * 校验token地址
     */
    private String checkTokenUrl;

    /**
     * QQ用户信息地址
     */
    private String userInfoUrl;
}
