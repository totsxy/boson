package org.boson.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.var;
import org.apache.ibatis.reflection.MetaObject;
import org.boson.domain.dto.UserDetailDto;
import org.boson.enums.ZoneEnum;
import org.boson.util.UserUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;


/**
 * mybatis plus自动填充
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Component
public class AutoFillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        UserDetailDto userDetailDto = UserUtils.getLoginUser();
        this.strictInsertFill(metaObject, "createBy", Integer.class, userDetailDto == null ? 0 :userDetailDto.getId());
        this.strictInsertFill(metaObject, "createAt", LocalDateTime.class, LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        UserDetailDto userDetailDto = UserUtils.getLoginUser();
        this.strictInsertFill(metaObject, "updateBy", Integer.class, userDetailDto == null ? 0 :userDetailDto.getId());
        this.strictUpdateFill(metaObject, "updateAt", LocalDateTime.class, LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
    }
}
