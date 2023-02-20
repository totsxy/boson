package org.boson.support.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;

public abstract class LambdaActuator<B, T extends LambdaActuator<B, T>> extends AbstractLambdaWrapper<B, T> {

    protected final LambdaCallable<B> callable;

    public LambdaActuator(LambdaCallable<B> callable) {
        this.callable = callable;
    }
}
