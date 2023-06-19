package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * QQ token信息DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQTokenDto {

    /**
     * openid
     */
    private String openid;

    /**
     * 客户端id
     */
    private String client_id;
}
