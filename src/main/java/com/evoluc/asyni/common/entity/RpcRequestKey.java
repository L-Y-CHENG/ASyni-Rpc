package com.evoluc.asyni.common.entity;

import lombok.EqualsAndHashCode;

/**
 * 服务方法Key
 */
@EqualsAndHashCode
public class RpcRequestKey {

    /**
     * 类名
     */
    private final String className;
    /**
     * 方法名
     */
    private final String methodName;
    /**
     * 参数类型
     */
    private final Class<?>[] types;

    public RpcRequestKey (RpcRequest request) {
        this.className = request.getClassName();
        this.methodName = request.getMethodName();
        this.types = request.getArgTypes();
    }
}
