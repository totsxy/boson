package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.UserAreaDto;
import org.boson.domain.dto.UserBackDto;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.po.UserAuth;
import org.boson.domain.vo.*;
import org.boson.support.mybatisplus.service.LambdaCallable;

import java.util.List;


/**
 * 用户账号服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface UserAuthService extends LambdaCallable<UserAuth> {

    /**
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     */
    void sendCode(String username);

    /**
     * 用户注册
     *
     * @param userVo 用户对象
     */
    boolean register(UserVo userVo);

    /**
     * 修改密码
     *
     * @param userVo 用户对象
     */
    boolean updatePassword(UserVo userVo);

    /**
     * 修改管理员密码
     *
     * @param passwordVo 密码对象
     */
    boolean updateAdminPassword(PasswordVo passwordVo);

    /**
     * qq登录
     *
     * @param qqLoginVo qq登录信息
     * @return 用户登录信息
     */
    UserInfoDto qqLogin(QQLoginVo qqLoginVo);

    /**
     * 微博登录
     *
     * @param weiboLoginVo 微博登录信息
     * @return 用户登录信息
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
     * 获取用户区域分布
     *
     * @param conditionVo 查询条件
     * @return 用户区域分布
     */
    List<UserAreaDto> listUserAreas(ConditionVo conditionVo);
}
