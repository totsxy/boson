package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.UserAreaDto;
import org.boson.domain.dto.UserBackDto;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.po.UserAuth;
import org.boson.domain.vo.*;
import org.boson.support.service.BaseService;

import java.util.List;


/**
 * 用户账户服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface UserAuthService extends BaseService<UserAuth> {

    /**
     * 发送邮箱验证码
     *
     * @param username 用户名
     */
    void sendCode(String username);

    /**
     * 用户注册
     *
     * @param userVo 用户信息
     */
    boolean register(UserVo userVo);

    /**
     * 修改密码
     *
     * @param userVo 用户信息
     */
    boolean updatePassword(UserVo userVo);

    /**
     * 修改管理员密码
     *
     * @param passwordVo 密码信息
     */
    boolean updateAdminPassword(PasswordVo passwordVo);

    /**
     * qq登录
     *
     * @param qqLoginVo qq登录信息
     * @return 用户信息
     */
    UserInfoDto qqLogin(QQLoginVo qqLoginVo);

    /**
     * 微博登录
     *
     * @param weiboLoginVo 微博登录信息
     * @return 用户信息
     */
    UserInfoDto weiboLogin(WeiboLoginVo weiboLoginVo);

    /**
     * 分页查询后台用户列表
     *
     * @param conditionVo 查询条件
     * @return 后台用户列表
     */
    PageResult<UserBackDto> pageUserBacks(ConditionVo conditionVo);

    /**
     * 获取用户区域分布列表
     *
     * @param conditionVo 查询条件
     * @return 用户区域分布列表
     */
    List<UserAreaDto> listUserAreas(ConditionVo conditionVo);
}
