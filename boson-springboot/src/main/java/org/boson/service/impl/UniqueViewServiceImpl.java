package org.boson.service.impl;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.boson.constant.RedisPrefixConstants;
import org.boson.domain.dto.UniqueViewDto;
import org.boson.domain.po.UniqueView;
import org.boson.mapper.UniqueViewMapper;
import org.boson.service.RedisService;
import org.boson.service.UniqueViewService;
import org.boson.enums.ZoneEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;


/**
 * 访问量统计服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Autowired
    private RedisService redisService;

    @Override
    public List<UniqueViewDto> listUniqueViews() {
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        return this.getBaseMapper().listUniqueViews(startTime, endTime);
    }

    @Scheduled(cron = " 0 0 0 * * ?", zone = "Asia/Shanghai")
    public void saveUniqueView() {
        // 获取每天用户量
        Long count = this.redisService.sSize(RedisPrefixConstants.UNIQUE_VISITOR);
        // 获取昨天日期插入数据
        UniqueView uniqueView = UniqueView.builder()
                .createAt(LocalDateTimeUtil.offset(LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())), -1, ChronoUnit.DAYS))
                .viewsCount(Optional.of(count.intValue()).orElse(0))
                .build();

        this.save(uniqueView);
    }

    @Scheduled(cron = " 0 1 0 * * ?", zone = "Asia/Shanghai")
    public void clear() {
        // 清空redis访客记录
        this.redisService.del(RedisPrefixConstants.UNIQUE_VISITOR);
        // 清空redis游客区域统计
        this.redisService.del(RedisPrefixConstants.VISITOR_AREA);
    }

}
