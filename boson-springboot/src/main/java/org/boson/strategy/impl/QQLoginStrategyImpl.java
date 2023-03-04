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
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Service("qqLoginStrategyImpl")
public class QQLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {
    @Autowired
    private QQConfigProperties qqConfigProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public SocialTokenDto getSocialToken(String data) {
        QQLoginVo qqLoginVO = JSON.parseObject(data, QQLoginVo.class);
        // 校验QQ token信息
        checkQQToken(qqLoginVO);
        // 返回token信息
        return SocialTokenDto.builder()
                .openId(qqLoginVO.getOpenId())
                .accessToken(qqLoginVO.getAccessToken())
                .loginType(LoginTypeEnum.QQ.getType())
                .build();
    }

    @Override
    public SocialUserInfoDto getSocialUserInfo(SocialTokenDto socialTokenDTO) {
        // 定义请求参数
        Map<String, String> formData = new HashMap<>(3);
        formData.put(SocialLoginConstants.QQ_OPEN_ID, socialTokenDTO.getOpenId());
        formData.put(SocialLoginConstants.ACCESS_TOKEN, socialTokenDTO.getAccessToken());
        formData.put(SocialLoginConstants.OAUTH_CONSUMER_KEY, qqConfigProperties.getAppId());
        // 获取QQ返回的用户信息
        QQUserInfoDto qqUserInfoDTO = JSON.parseObject(restTemplate.getForObject(qqConfigProperties.getUserInfoUrl(), String.class, formData), QQUserInfoDto.class);
        // 返回用户信息
        return SocialUserInfoDto.builder()
                .nickname(Objects.requireNonNull(qqUserInfoDTO).getNickname())
                .avatar(qqUserInfoDTO.getFigureurl_qq_1())
                .build();
    }

    /**
     * 校验qq token信息
     *
     * @param qqLoginVO qq登录信息
     */
    private void checkQQToken(QQLoginVo qqLoginVO) {
        // 根据token获取qq openId信息
        Map<String, String> qqData = new HashMap<>(1);
        qqData.put(SocialLoginConstants.ACCESS_TOKEN, qqLoginVO.getAccessToken());
        try {
            String result = restTemplate.getForObject(qqConfigProperties.getCheckTokenUrl(), String.class, qqData);
            QQTokenDto qqTokenDTO = JSON.parseObject(CommonUtils.getBracketsContent(Objects.requireNonNull(result)), QQTokenDto.class);
            // 判断openId是否一致
            if (!qqLoginVO.getOpenId().equals(qqTokenDTO.getOpenid())) {
                throw new BizException(StatusCodeEnum.QQ_LOGIN_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(StatusCodeEnum.QQ_LOGIN_ERROR);
        }
    }

}
