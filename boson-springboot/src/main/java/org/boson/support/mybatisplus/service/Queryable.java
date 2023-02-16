package org.boson.support.mybatisplus.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.boson.domain.PageResult;
import org.boson.util.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;


public interface Queryable<T> extends IService<T> {

    default <R> R getOneAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz) {
        return BeanUtils.bean2Bean(this.getOne(Objects.requireNonNull(queryWrapper)), clazz);
    }

    default List<T> queryList(Wrapper<T> queryWrapper) {
        return this.list(Objects.requireNonNull(queryWrapper));
    }

    default <R> List<R> queryListAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz, Function<R, R> mapper) {
        return BeanUtils.bean2Bean(this.queryList(queryWrapper), clazz)
                .stream()
                .map(Optional.ofNullable(mapper).orElse(Function.identity()))
                .collect(Collectors.toList());
    }

    default <R> List<R> queryListAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz) {
        return this.queryListAndPo2Vo(queryWrapper, clazz, null);
    }

    default PageResult<T> queryPage(Wrapper<T> queryWrapper) {
        Page<T> page = this.page(getPage(), Objects.requireNonNull(queryWrapper));
        return PageResult.of(page);
    }

    default <R> PageResult<R> queryPageAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz, Function<R, R> mapper) {
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

    default <R> PageResult<R> queryPageAndPo2Vo(Wrapper<T> queryWrapper, Class<R> clazz) {
        return this.queryPageAndPo2Vo(queryWrapper, clazz, null);
    }

    default LambdaQueryActuator<T> beginQuery() {
        return new LambdaQueryActuator<>(this);
    }

    Page<T> getPage();
}
