package com.evoluc.asyni.rpc;


import com.evoluc.asyni.rpc.client.ClientConfig;
import com.evoluc.asyni.common.entity.RpcRequest;
import com.evoluc.asyni.common.entity.RpcResponse;
import com.evoluc.asyni.rpc.client.RequestPromise;
import com.evoluc.asyni.rpc.client.RpcClientTransport;
import com.evoluc.asyni.util.SnowflakeIdWorker;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Data
public class RpcInvocationHandler implements InvocationHandler {

    private RpcClientTransport clientTransport;

    private ClientConfig config;

    private String name;

    public RpcInvocationHandler (RpcClientTransport clientTransport, ClientConfig config, String name) {
        this.clientTransport = clientTransport;
        this.config = config;
        this.name = name;
    }

    @Override
    public Object invoke (Object proxy, Method method, Object[] args) throws Throwable {

        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        RequestPromise<?> promise = new RequestPromise<>(method.getReturnType(), new CompletableFuture<>());
        RpcRequest request = RpcRequest.builder()
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .argTypes(method.getParameterTypes())
                .args(args)
                .id(SnowflakeIdWorker.defaultNextId())
                .name(name)
                .build();

        clientTransport.send(request, promise);
        return result(promise);
    }


    private Object result(RequestPromise<?> promise) throws Throwable {
        Class<?> clazz = promise.getClazz();
        CompletableFuture<RpcResponse> responseFuture = promise.getResponseFuture();
        RpcResponse rpcResponse = responseFuture.get(config.getTimeout(), TimeUnit.SECONDS);
        if (rpcResponse.hasError()){
            throw rpcResponse.getException();
        }else {
            return clazz.cast(rpcResponse.getResult());
        }
    }


}
