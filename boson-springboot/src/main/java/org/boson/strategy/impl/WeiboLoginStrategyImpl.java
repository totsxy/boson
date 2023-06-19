package org.boson.strategy.impl;

import com.alibaba.fastjson.JSON;
import org.boson.config.WeiboConfigProperties;
import org.boson.constant.SocialLoginConstants;
import org.boson.domain.dto.SocialTokenDto;
import org.boson.domain.dto.SocialUserInfoDto;
import org.boson.domain.dto.WeiboTokenDto;
import org.boson.domain.dto.WeiboUserInfoDto;
import org.boson.enums.LoginTypeEnum;
import org.boson.exception.BizException;
import org.boson.domain.vo.WeiboLoginVo;
import org.boson.enums.StatusCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * 微博登录策略实现
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service("weiboLoginStrategyImpl")
public class WeiboLoginStrategyImpl extends AbstractSocialLoginStrategyImpl {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WeiboConfigProperties weiboConfigProperties;

    @Override
    public SocialTokenDto getSocialToken(String data) {
        WeiboLoginVo weiboLoginVo = JSON.parseObject(data, WeiboLoginVo.class);
        WeiboTokenDto weiboToken = this.getWeiboToken(weiboLoginVo);

        return SocialTokenDto.builder()
                .openId(weiboToken.getUid())
                .accessToken(weiboToken.getAccess_token())
                .loginType(LoginTypeEnum.WEIBO.getType())
                .build();
    }

    @Override
    public SocialUserInfoDto getSocialUserInfo(SocialTokenDto socialTokenDto) {
        Map<String, String> fromData = new HashMap<>(2);
        fromData.put(SocialLoginConstants.UID, socialTokenDto.getOpenId());
        fromData.put(SocialLoginConstants.ACCESS_TOKEN, socialTokenDto.getAccessToken());

        WeiboUserInfoDto weiboUserInfoDto = this.restTemplate.getForObject(this.weiboConfigProperties.getUserInfoUrl(), WeiboUserInfoDto.class, fromData);
        return SocialUserInfoDto.builder()
                .nickname(Objects.requireNonNull(weiboUserInfoDto).getScreen_name())
                .avatar(weiboUserInfoDto.getAvatar_hd())
                .build();
    }

    /**
     * 获取微博token信息
     *
     * @param weiboLoginVo 微博登录信息
     * @return {@link WeiboTokenDto} 微博token
     */
    private WeiboTokenDto getWeiboToken(WeiboLoginVo weiboLoginVo) {
        // 根据code换取微博uid和accessToken
        MultiValueMap<String, String> fromData = new LinkedMultiValueMap<>();
        // 定义微博token请求参数
        fromData.add(SocialLoginConstants.CLIENT_ID, this.weiboConfigProperties.getAppId());
        fromData.add(SocialLoginConstants.CLIENT_SECRET, this.weiboConfigProperties.getAppSecret());
        fromData.add(SocialLoginConstants.GRANT_TYPE, this.weiboConfigProperties.getGrantType());
        fromData.add(SocialLoginConstants.REDIRECT_URI, this.weiboConfigProperties.getRedirectUrl());
        fromData.add(SocialLoginConstants.CODE, weiboLoginVo.getCode());

        try {
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(fromData, null);
            return this.restTemplate.exchange(this.weiboConfigProperties.getAccessTokenUrl(), HttpMethod.POST, requestEntity, WeiboTokenDto.class).getBody();
        } catch (Exception e) {
            throw new BizException(StatusCodeEnum.WEIBO_LOGIN_ERROR.getCode(), e.getMessage());
        }
    }
}
