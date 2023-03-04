package org.boson.support.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;

public abstract class LambdaActuator<B, T extends LambdaActuator<B, T>> extends AbstractLambdaWrapper<B, T> {

    protected final BaseService<B> service;

    public LambdaActuator(BaseService<B> service) {
        this.service = service;
    }
}
