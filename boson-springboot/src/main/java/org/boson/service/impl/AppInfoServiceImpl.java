package org.boson.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import eu.bitwalker.useragentutils.UserAgent;
import org.boson.domain.dto.AppBackInfoDto;
import org.boson.domain.dto.AppHomeInfoDto;
import org.boson.domain.dto.UniqueViewDto;
import org.boson.domain.po.AppInfo;
import org.boson.domain.vo.AppInfoVo;
import org.boson.domain.vo.AppConfigVo;
import org.boson.mapper.AppInfoMapper;
import org.boson.service.AppInfoService;
import org.boson.service.RedisService;
import org.boson.service.UniqueViewService;
import org.boson.service.UserInfoService;
import org.boson.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.boson.constant.CommonConstants.*;
import static org.boson.constant.RedisPrefixConstants.*;


/**
 * 博客信息服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class AppInfoServiceImpl extends ServiceImpl<AppInfoMapper, AppInfo> implements AppInfoService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UniqueViewService uniqueViewService;
    @Autowired
    private UserInfoService userInfoService;
    @Resource
    private HttpServletRequest request;

    @Override
    public AppHomeInfoDto getAppHomeInfo() {
        return AppHomeInfoDto.builder()
                .viewCount(this.getViewCount())
                .appConfig(this.getAppConfig())
                .build();
    }

    @Override
    public AppBackInfoDto getAppBackInfo() {
        Integer viewCount = this.getViewCount();
        Integer userCount = this.userInfoService.count(null);
        List<UniqueViewDto> uniqueViewDtoList = this.uniqueViewService.listUniqueViews();

        return AppBackInfoDto.builder()
                .viewCount(viewCount)
                .userCount(userCount)
                .uniqueViewDtoList(uniqueViewDtoList)
                .build();
    }

    @Override
    public boolean saveOrUpdateAppConfig(AppConfigVo appConfigVo) {
        AppInfo appInfo = AppInfo.builder()
                .id(DEFAULT_CONFIG_ID)
                .config(JSON.toJSONString(appConfigVo))
                .build();

        this.updateById(appInfo);
        return this.redisService.del(APP_CONFIG);
    }

    @Override
    public AppConfigVo getAppConfig() {
        AppConfigVo appConfigVo;
        Object appConfig = this.redisService.get(APP_CONFIG);
        if (Objects.nonNull(appConfig)) {
            appConfigVo = JSON.parseObject(appConfig.toString(), AppConfigVo.class);
        } else {
            String config = this.getById(DEFAULT_CONFIG_ID).getConfig();
            appConfigVo = JSON.parseObject(config, AppConfigVo.class);
            this.redisService.set(APP_CONFIG, config);
        }
        return appConfigVo;
    }

    @Override
    public boolean updateAbout(AppInfoVo appInfoVo) {
        this.redisService.set(ABOUT, appInfoVo.getAboutContent());
        return true;
    }

    @Override
    public String getAbout() {
        Object value = this.redisService.get(ABOUT);
        return Optional.ofNullable(value).orElse("").toString();
    }

    @Override
    public void report() {
        String ipAddress = IpUtils.getIpAddress(this.request);
        UserAgent userAgent = IpUtils.getUserAgent(this.request);
        String uuid = ipAddress + userAgent.getBrowser().getName() + userAgent.getOperatingSystem().getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());

        // 判断是否访问
        if (!this.redisService.sIsMember(UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getIpSource(ipAddress);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(PROVINCE, "")
                        .replaceAll(CITY, "");
                this.redisService.hIncr(VISITOR_AREA, ipSource, 1L);
            } else {
                this.redisService.hIncr(VISITOR_AREA, UNKNOWN, 1L);
            }

            // 访问量+1
            this.redisService.incr(VIEW_COUNT, 1);
            // 保存唯一标识
            this.redisService.sAdd(UNIQUE_VISITOR, md5);
        }
    }

    private Integer getViewCount() {
        Object count = this.redisService.get(VIEW_COUNT);
        return Integer.parseInt(Optional.ofNullable(count).orElse(0).toString());
    }
}
