package com.evoluc.asyni.common.entity;


import java.lang.reflect.Method;

/**
 * 服务信息
 */
public class RpcServerBody {

    private final Object service;
    private final Method method;

    public RpcServerBody (Object service, Method method) {
        this.service = service;
        this.method = method;
    }

    public Object service () {
        return service;
    }

    public Method method () {
        return method;
    }
}
