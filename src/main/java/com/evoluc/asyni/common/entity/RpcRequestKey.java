package com.evoluc.asyni.common.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;

/**
 * 服务方法Key
 */
@Builder
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
    /**
     * 服务名称
     */
    private String name;

    public RpcRequestKey (RpcRequest request) {
        this.className = request.getClassName();
        this.methodName = request.getMethodName();
        this.types = request.getArgTypes();
        this.name = request.getName();
    }

    public RpcRequestKey (String className, String methodName, Class<?>[] types, String name) {
        this.className = className;
        this.methodName = methodName;
        this.types = types;
        this.name = name;
    }
}
