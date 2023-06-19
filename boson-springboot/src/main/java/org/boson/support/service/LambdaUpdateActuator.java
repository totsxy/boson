package org.boson.support.service;

import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.ArrayList;
import java.util.List;


public class LambdaUpdateActuator<T> extends LambdaActuator<T, LambdaUpdateActuator<T>>
        implements Update<LambdaUpdateActuator<T>, SFunction<T, ?>> {

    private final List<String> sqlSet;

    public LambdaUpdateActuator(BaseService<T> baseService) {
        super(baseService);

        super.setEntity(null);
        super.initNeed();
        this.sqlSet = new ArrayList<>();
    }

    @Override
    protected LambdaUpdateActuator<T> instance() {
        return new LambdaUpdateActuator<>(this.service);
    }

    @Override
    public LambdaUpdateActuator<T> set(boolean condition, SFunction<T, ?> column, Object val) {
        if (condition) {
            this.sqlSet.add(String.format("%s=%s", this.columnToString(column), this.formatSql("{0}", val)));
        }
        return this.typedThis;
    }

    @Override
    public LambdaUpdateActuator<T> setSql(boolean condition, String sql) {
        if (condition && StringUtils.isNotBlank(sql)) {
            this.sqlSet.add(sql);
        }
        return this.typedThis;
    }

    @Override
    public String getSqlSet() {
        return CollectionUtils.isEmpty(this.sqlSet) ? null : String.join(",", this.sqlSet);
    }

    public boolean update(T entity) {
        return this.service.update(entity, this);
    }

    public boolean update() {
        T entity = (T) ReflectUtil.newInstance(ReflectionKit.getSuperClassGenericType(this.getClass(), 0));
        return this.update(entity);
    }
}
