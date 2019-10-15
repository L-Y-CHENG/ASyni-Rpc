package com.evoluc.asyni.common.entity;

import lombok.Builder;
import lombok.Data;

@Data
public class RpcResponse {

    /**
     * 消息的唯一标识
     */
    private long id;
    /**
     * 结果
     */
    private Object result;
    /**
     * 异常
     */
    private Throwable exception;

    public RpcResponse (long id) {
        this.id = id;
    }



    /**
     * 是否调用异常
     *
     * @return true表示调用异常
     */
    public boolean hasError() {
        return exception != null;
    }


}
