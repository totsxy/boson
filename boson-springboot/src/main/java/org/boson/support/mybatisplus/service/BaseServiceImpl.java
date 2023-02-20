package org.boson.support.mybatisplus.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.boson.util.PageUtils;

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements LambdaCallable<T> {

    @Override
    public Page<T> getPage() {
        return new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
    }
}
