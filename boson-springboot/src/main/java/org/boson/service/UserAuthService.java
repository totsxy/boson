package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.UserAreaDto;
import org.boson.domain.dto.UserBackDto;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.po.UserAuth;
import org.boson.domain.vo.*;
import org.boson.support.mybatisplus.service.Queryable;

import java.util.List;


/**
 * 用户账号服务
 *
 * @author yezhiqiu
 * @date 2021/07/29
 */
public interface UserAuthService extends Queryable<UserAuth> {

    /**
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     */
    void sendCode(String username);

    /**
     * 获取用户区域分布
     *
     * @param conditionVO 条件签证官
     * @return {@link List<  UserAreaDto  >} 用户区域分布
     */
    List<UserAreaDto> listUserAreas(ConditionVo conditionVO);

    /**
     * 用户注册
     *
     * @param user 用户对象
     */
    void register(UserVo user);

    /**
     * qq登录
     *
     * @param qqLoginVO qq登录信息
     * @return 用户登录信息
     */
    UserInfoDto qqLogin(QQLoginVo qqLoginVO);

    /**
     * 微博登录
     *
     * @param weiboLoginVO 微博登录信息
     * @return 用户登录信息
     */
    UserInfoDto weiboLogin(WeiboLoginVo weiboLoginVO);

    /**
     * 修改密码
     *
     * @param user 用户对象
     */
    void updatePassword(UserVo user);

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码对象
     */
    void updateAdminPassword(PasswordVo passwordVO);

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageResult<UserBackDto> listUserBackDTO(ConditionVo condition);

}
