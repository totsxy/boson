package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.vo.*;
import org.boson.domain.dto.UserOnlineDto;
import org.boson.domain.po.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.boson.support.mybatisplus.service.LambdaCallable;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户信息服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface UserInfoService extends LambdaCallable<UserInfo> {

    /**
     * 修改用户资料
     *
     * @param userInfoVo 用户资料
     */
    boolean updateUserInfo(UserInfoVo userInfoVo);

    /**
     * 修改用户头像
     *
     * @param file 头像图片
     * @return 头像地址
     */
    boolean updateUserAvatar(MultipartFile file);

    /**
     * 绑定用户邮箱
     *
     * @param emailVo 邮箱
     */
    boolean saveUserEmail(EmailVo emailVo);

    /**
     * 更新用户角色
     *
     * @param userRoleVo 更新用户角色
     */
    boolean updateUserRole(UserRoleVo userRoleVo);

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVo 用户禁用信息
     */
    boolean updateUserDisable(UserDisableVo userDisableVo);

    /**
     * 查看在线用户列表
     *
     * @param conditionVo 查询条件
     * @return 在线用户列表
     */
    PageResult<UserOnlineDto> listOnlineUsers(ConditionVo conditionVo);

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息id
     */
    void removeOnlineUser(Integer userInfoId);
}
