package org.boson.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.boson.domain.PageResult;
import org.boson.util.BeanUtils;
import org.boson.util.PageUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    public <R> R getOneAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz) {
        return BeanUtils.bean2Bean(this.getOne(Objects.requireNonNull(queryWrapper)), clazz);
    }

    public List<T> queryList(Wrapper<T> queryWrapper) {
        return this.list(Objects.requireNonNull(queryWrapper));
    }

    public <R> List<R> queryListAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz, Function<R, R> mapper) {
        return  BeanUtils.bean2Bean(this.queryList(queryWrapper), clazz)
                .stream()
                .map(Optional.ofNullable(mapper).orElse(Function.identity()))
                .collect(Collectors.toList());
    }

    public <R> List<R> queryListAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz) {
        return this.queryListAndPo2Vo(queryWrapper, clazz, null);
    }

    public PageResult<T> queryPage(Wrapper<T> queryWrapper) {
        Page<T> page = this.page(getPage(), Objects.requireNonNull(queryWrapper));
        return PageResult.of(page);
    }

    public <R> PageResult<R> queryPageAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz, Function<R, R> mapper) {
        Page<T> page = this.page(getPage(), Objects.requireNonNull(queryWrapper));
        List<T> records = page.getRecords();

        List<R> resVoList;
        if (CollectionUtil.isEmpty(records)) {
            resVoList = CollectionUtil.newArrayList();
        } else {
            resVoList = BeanUtils.bean2Bean(records, clazz)
                    .stream()
                    .filter(Objects::nonNull)
                    .map(Optional.ofNullable(mapper).orElse(Function.identity()))
                    .collect(Collectors.toList());
        }

        return PageResult.of(resVoList, page.getTotal());
    }

    public <R> PageResult<R> queryPageAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz) {
        return this.queryPageAndPo2Vo(queryWrapper, clazz, null);
    }

    public final InnerLambdaQueryWrapper beginQuery() {
        return new InnerLambdaQueryWrapper(this);
    }

    protected Page<T> getPage() {
        return new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
    }

    public final class InnerLambdaQueryWrapper extends AbstractLambdaWrapper<T, InnerLambdaQueryWrapper>
            implements Query<InnerLambdaQueryWrapper, T, SFunction<T, ?>> {

        private final BaseServiceImpl<M, T> service;
        private final SharedString sqlSelect;

        private InnerLambdaQueryWrapper(BaseServiceImpl<M, T> service) {
            this.service = service;
            this.sqlSelect = new SharedString();

            super.setEntity(null);
            super.initNeed();
        }

        @Override
        protected InnerLambdaQueryWrapper instance() {
            return new InnerLambdaQueryWrapper(this.service);
        }

        @Override
        public String getSqlSelect() {
            return this.sqlSelect.getStringValue();
        }

        @SafeVarargs
        @Override
        public final InnerLambdaQueryWrapper select(SFunction<T, ?>... columns) {
            if (ArrayUtils.isNotEmpty(columns)) {
                this.sqlSelect.setStringValue(this.columnsToString(false, columns));
            }
            return this.typedThis;
        }

        @Override
        public InnerLambdaQueryWrapper select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
            super.setEntityClass(entityClass);
            this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(this.getEntityClass()).chooseSelect(predicate));
            return this.typedThis;
        }

        public T getOne() {
            return this.service.getOne(this);
        }

        public <R> R getOneAndPo2Vo(Class<R> clazz) {
            return this.service.getOneAndPo2Vo(this, Objects.requireNonNull(clazz));
        }

        public int count() {
            return this.service.count(this);
        }

        public List<T> queryList() {
            return this.service.queryList(this);
        }

        public <R> List<R> queryListAndPo2Vo(Class<R> clazz, Function<R, R> mapper) {
            return this.service.queryListAndPo2Vo(this, Objects.requireNonNull(clazz), mapper);
        }

        public <R> List<R> queryListAndPo2Vo(Class<R> clazz) {
            return this.queryListAndPo2Vo(clazz, null);
        }

        public PageResult<T> queryPage() {
            return this.service.queryPage(this);
        }

        public <R> PageResult<R> queryPageAndPo2Vo(Class<R> clazz, Function<R, R> mapper) {
            return this.service.queryPageAndPo2Vo(this, Objects.requireNonNull(clazz), mapper);
        }

        public <R> PageResult<R> queryPageAndPo2Vo(Class<R> clazz) {
            return this.queryPageAndPo2Vo(clazz, null);
        }
    }
}
