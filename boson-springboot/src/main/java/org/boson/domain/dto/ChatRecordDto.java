package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.boson.domain.po.ChatRecord;

import java.util.List;


/**
 * 聊天记录
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRecordDto {

    /**
     * 聊天记录
     */
    private List<ChatRecord> chatRecordList;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * ip来源
     */
    private String ipSource;

}
