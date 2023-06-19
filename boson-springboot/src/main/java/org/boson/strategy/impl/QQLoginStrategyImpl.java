package org.boson.strategy.impl;

import com.alibaba.fastjson.JSON;
import org.boson.config.QQConfigProperties;
import org.boson.constant.SocialLoginConstants;
import org.boson.domain.dto.QQTokenDto;
import org.boson.domain.dto.QQUserInfoDto;
import org.boson.domain.dto.SocialTokenDto;
import org.boson.domain.dto.SocialUserInfoDto;
import org.boson.enums.LoginTypeEnum;
import org.boson.exception.BizException;
import org.boson.domain.vo.QQLoginVo;
import org.boson.enums.StatusCodeEnum;
import org.boson.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * qq登录策略实现
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service("qqLoginStrategyImpl")
public class QQLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private QQConfigProperties qqConfigProperties;

    @Override
    public SocialTokenDto getSocialToken(String data) {
        // 校验token信息
        QQLoginVo qqLoginVo = JSON.parseObject(data, QQLoginVo.class);
        this.checkQQToken(qqLoginVo);

        return SocialTokenDto.builder()
                .openId(qqLoginVo.getOpenId())
                .accessToken(qqLoginVo.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .build();
    }

    @Override
    public SocialUserInfoDto getSocialUserInfo(SocialTokenDto socialTokenDto) {
        Map<String, String> formData = new HashMap<>(3);
        formData.put(SocialLoginConstants.QQ_OPEN_ID, socialTokenDto.getOpenId());
        formData.put(SocialLoginConstants.ACCESS_TOKEN, socialTokenDto.getAccessToken());
        formData.put(SocialLoginConstants.OAUTH_CONSUMER_KEY, this.qqConfigProperties.getAppId());

        QQUserInfoDto qqUserInfoDto = JSON.parseObject(this.restTemplate.getForObject(this.qqConfigProperties.getUserInfoUrl(), String.class, formData), QQUserInfoDto.class);
        return SocialUserInfoDto.builder()
                .nickname(Objects.requireNonNull(qqUserInfoDto).getNickname())
                .avatar(qqUserInfoDto.getFigureurl_qq_1())
                .build();
    }

    /**
     * 校验token信息
     *
     * @param qqLoginVo qq登录信息
     */
    private void checkQQToken(QQLoginVo qqLoginVo) {
        Map<String, String> fromData = new HashMap<>(1);
        fromData.put(SocialLoginConstants.ACCESS_TOKEN, qqLoginVo.getAccessToken());

        try {
            // 根据token获取openId信息
            String result = this.restTemplate.getForObject(this.qqConfigProperties.getCheckTokenUrl(), String.class, fromData);
            QQTokenDto qqTokenDto = JSON.parseObject(CommonUtils.getBracketsContent(Objects.requireNonNull(result)), QQTokenDto.class);

            // 判断openId是否一致
            if (!qqLoginVo.getOpenId().equals(qqTokenDto.getOpenid())) {
                throw new BizException(StatusCodeEnum.QQ_LOGIN_ERROR);
            }
        } catch (Exception e) {
            throw new BizException(StatusCodeEnum.QQ_LOGIN_ERROR.getCode(), e.getMessage());
        }
    }
}
