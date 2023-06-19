package org.boson.constant;

/**
 * redis常量
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public final class RedisPrefixConstants {

    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 15 * 60;

    /**
     * 验证码
     */
    public static final String USER_CODE_KEY = "code:";

    /**
     * 网站配置
     */
    public static final String APP_CONFIG = "app_config";

    /**
     * 关于我信息
     */
    public static final String ABOUT = "about";

    /**
     * 用户地区
     */
    public static final String USER_AREA = "user_area";

    /**
     * 访客地区
     */
    public static final String VISITOR_AREA = "visitor_area";

    /**
     * 访客
     */
    public static final String UNIQUE_VISITOR = "unique_visitor";

    /**
     * 访问量
     */
    public static final String VIEW_COUNT = "view_count";
}
