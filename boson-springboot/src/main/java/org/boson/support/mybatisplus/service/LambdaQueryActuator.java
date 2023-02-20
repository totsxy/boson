package org.boson.support.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.boson.domain.PageResult;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaQueryActuator<T> extends LambdaActuator<T, LambdaQueryActuator<T>>
        implements Query<LambdaQueryActuator<T>, T, SFunction<T, ?>> {

    private final SharedString sqlSelect;

    public LambdaQueryActuator(LambdaCallable<T> lambdaCallable) {
        super(lambdaCallable);
        this.sqlSelect = new SharedString();
        super.setEntity(null);
        super.initNeed();
    }

    @Override
    protected LambdaQueryActuator<T> instance() {
        return new LambdaQueryActuator<>(this.callable);
    }

    @SafeVarargs
    @Override
    public final LambdaQueryActuator<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(this.columnsToString(false, columns));
        }
        return this.typedThis;
    }

    @Override
    public LambdaQueryActuator<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        super.setEntityClass(entityClass);
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(this.getEntityClass()).chooseSelect(predicate));
        return this.typedThis;
    }

    @Override
    public String getSqlSelect() {
        return this.sqlSelect.getStringValue();
    }

    public boolean remove() {
        return this.callable.remove(this);
    }

    public T getOne() {
        return this.callable.getOne(this);
    }

    public <R> R getOneAndPo2Vo(Class<R> clazz) {
        return this.callable.getOneAndPo2Vo(this, Objects.requireNonNull(clazz));
    }

    public int count() {
        return this.callable.count(this);
    }

    public List<T> queryList() {
        return this.callable.queryList(this);
    }

    public <R> List<R> queryListAndPo2Vo(Class<R> clazz, Function<R, R> mapper) {
        return this.callable.queryListAndPo2Vo(this, Objects.requireNonNull(clazz), mapper);
    }

    public <R> List<R> queryListAndPo2Vo(Class<R> clazz) {
        return this.queryListAndPo2Vo(clazz, null);
    }

    public PageResult<T> queryPage() {
        return this.callable.queryPage(this);
    }

    public <R> PageResult<R> queryPageAndPo2Vo(Class<R> clazz, Function<R, R> mapper) {
        return this.callable.queryPageAndPo2Vo(this, Objects.requireNonNull(clazz), mapper);
    }

    public <R> PageResult<R> queryPageAndPo2Vo(Class<R> clazz) {
        return this.queryPageAndPo2Vo(clazz, null);
    }
}
