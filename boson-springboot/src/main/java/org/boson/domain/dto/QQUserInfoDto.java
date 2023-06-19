package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * QQ用户信息DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQUserInfoDto {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * qq头像
     */
    private String figureurl_qq_1;
}
