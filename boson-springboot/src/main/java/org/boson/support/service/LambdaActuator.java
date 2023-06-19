package org.boson.support.service;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;

public abstract class LambdaActuator<S, T extends LambdaActuator<S, T>> extends AbstractLambdaWrapper<S, T> {

    protected final BaseService<S> service;

    public LambdaActuator(BaseService<S> service) {
        this.service = service;
    }
}
