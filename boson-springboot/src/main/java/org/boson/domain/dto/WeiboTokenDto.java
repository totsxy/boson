package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 微博token信息DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeiboTokenDto {

    /**
     * 微博uid
     */
    private String uid;

    /**
     * 访问令牌
     */
    private String access_token;
}
