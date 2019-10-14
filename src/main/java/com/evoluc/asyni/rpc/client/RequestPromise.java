package com.evoluc.asyni.rpc.client;

import com.evoluc.asyni.common.entity.RpcResponse;
import lombok.Data;

import java.util.concurrent.CompletableFuture;

@Data
public class RequestPromise<T>  {

    private final CompletableFuture<RpcResponse> future;

    public RequestPromise (CompletableFuture<RpcResponse> future) {
        this.future = future;
    }



}
