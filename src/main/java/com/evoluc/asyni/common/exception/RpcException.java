package com.evoluc.asyni.common.exception;

public class RpcException extends Exception {

    public RpcException (String message) {
        super(message);
    }

    public RpcException (Throwable cause) {
        super(cause);
    }
}
