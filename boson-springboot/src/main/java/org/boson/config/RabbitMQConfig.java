package org.boson.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.boson.constant.MQPrefixConstants.*;


/**
 * Rabbitmq配置类
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue articleQueue() {
        return new Queue(MAXWELL_QUEUE, true);
    }

    @Bean
    public FanoutExchange maxWellExchange() {
        return new FanoutExchange(MAXWELL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingArticleDirect() {
        return BindingBuilder.bind(articleQueue()).to(maxWellExchange());
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public FanoutExchange emailExchange() {
        return new FanoutExchange(EMAIL_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingEmailDirect() {
        return BindingBuilder.bind(emailQueue()).to(emailExchange());
    }
}
