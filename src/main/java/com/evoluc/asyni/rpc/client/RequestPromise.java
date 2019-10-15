package com.evoluc.asyni.rpc.client;

import com.evoluc.asyni.common.entity.RpcResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.CompletableFuture;

@Data
public class RequestPromise<T>  {

    private final CompletableFuture<RpcResponse> responseFuture;

    private final Class<T> clazz;

    public RequestPromise (Class<T> clazz, CompletableFuture<RpcResponse> responseFuture) {
        this.responseFuture = responseFuture;
        this.clazz = clazz;
    }

}
