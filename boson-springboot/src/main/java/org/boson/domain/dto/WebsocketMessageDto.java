package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * websocket消息DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebsocketMessageDto {

    /**
     * 类型
     */
    private Integer type;

    /**
     * 数据
     */
    private Object data;
}
