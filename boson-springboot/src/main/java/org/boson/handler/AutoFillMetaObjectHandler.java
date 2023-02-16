package org.boson.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
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
        this.strictInsertFill(metaObject, "createBy", Integer.class, UserUtils.getLoginUser().getId());
        this.strictInsertFill(metaObject, "createAt", LocalDateTime.class, LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateBy", Integer.class, UserUtils.getLoginUser().getId());
        this.strictUpdateFill(metaObject, "updateAt", LocalDateTime.class, LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())));
    }
}
