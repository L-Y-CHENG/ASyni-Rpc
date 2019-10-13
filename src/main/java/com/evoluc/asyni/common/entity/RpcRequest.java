package com.evoluc.asyni.common.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RpcRequest {

    /**
     * 消息的唯一标识
     */
    private long id;
    /**
     * 接口类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] argTypes;
    /**
     * 入参参数
     */
    private Object[] args;

}
