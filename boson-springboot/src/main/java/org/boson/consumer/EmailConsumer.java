package org.boson.consumer;

import com.alibaba.fastjson.JSON;
import org.boson.domain.dto.EmailDto;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import static org.boson.constant.MQPrefixConstants.EMAIL_QUEUE;

/**
 * 通知邮箱
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Component
@RabbitListener(queues = EMAIL_QUEUE)
public class EmailConsumer {

    /**
     * 邮箱号
     */
    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    private  JavaMailSender javaMailSender;

    @RabbitHandler
    public void process(byte[] data) {
        EmailDto emailDto = JSON.parseObject(new String(data), EmailDto.class);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(emailDto.getTo());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getContent());
        javaMailSender.send(message);
    }
}
