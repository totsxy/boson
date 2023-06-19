package org.boson.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 微博用户信息DTO
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeiboUserInfoDto {

    /**
     * 昵称
     */
    private String screen_name;

    /**
     * 头像
     */
    private String avatar_hd;
}
