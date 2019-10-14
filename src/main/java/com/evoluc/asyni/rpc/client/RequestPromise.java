package com.evoluc.asyni.rpc.client;

import com.evoluc.asyni.common.entity.RpcResponse;
import lombok.Data;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Data
public class RequestPromise<T>  {

    private Consumer<T> onSuccess;

    private Consumer<Throwable> onFailure;

    private final CompletableFuture<RpcResponse> future;

    public RequestPromise (CompletableFuture<RpcResponse> future) {
        this.future = future;
    }



}
