package org.boson.service;

import org.boson.domain.dto.BlogBackInfoDto;
import org.boson.domain.dto.BlogHomeInfoDto;
import org.boson.domain.vo.BlogInfoVo;
import org.boson.domain.vo.WebsiteConfigVo;

/**
 * 博客信息服务
 *
 * @author yezhiqiu
 * @date 2021/08/09
 */
public interface BlogInfoService {

    /**
     * 获取首页数据
     *
     * @return 博客首页信息
     */
    BlogHomeInfoDto getBlogHomeInfo();

    /**
     * 获取后台首页数据
     *
     * @return 博客后台信息
     */
    BlogBackInfoDto getBlogBackInfo();

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    void updateWebsiteConfig(WebsiteConfigVo websiteConfigVO);

    /**
     * 获取网站配置
     *
     * @return {@link WebsiteConfigVo} 网站配置
     */
    WebsiteConfigVo getWebsiteConfig();

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    String getAbout();

    /**
     * 修改关于我内容
     *
     * @param blogInfoVO 博客信息
     */
    void updateAbout(BlogInfoVo blogInfoVO);

    /**
     * 上传访客信息
     */
    void report();

}
