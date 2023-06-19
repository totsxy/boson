package org.boson.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.boson.domain.dto.AppBackInfoDto;
import org.boson.domain.dto.AppHomeInfoDto;
import org.boson.domain.po.AppInfo;
import org.boson.domain.vo.AppInfoVo;
import org.boson.domain.vo.AppConfigVo;


/**
 * 应用信息服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface AppInfoService extends IService<AppInfo> {

    /**
     * 获取前台信息
     *
     * @return 应用前台信息
     */
    AppHomeInfoDto getAppHomeInfo();

    /**
     * 获取后台信息
     *
     * @return 应用后台信息
     */
    AppBackInfoDto getAppBackInfo();

    /**
     * 保存或更新应用配置
     *
     * @param appConfigVo 应用配置
     */
    boolean saveOrUpdateAppConfig(AppConfigVo appConfigVo);

    /**
     * 获取应用配置
     *
     * @return {@link AppConfigVo} 应用配置
     */
    AppConfigVo getAppConfig();

    /**
     * 修改关于我内容
     *
     * @param appInfoVo 博客信息
     */
    boolean updateAbout(AppInfoVo appInfoVo);

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    String getAbout();

    /**
     * 上传访客信息
     */
    void report();
}
