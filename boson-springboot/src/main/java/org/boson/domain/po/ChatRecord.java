package org.boson.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.boson.domain.Entity;

import java.time.LocalDateTime;

/**
 * 聊天记录
 *
 * @author yezhiqiu
 * @date 2021/07/29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@TableName("tb_chat_record")
public class ChatRecord extends Entity {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 用户登录ip
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;
}
