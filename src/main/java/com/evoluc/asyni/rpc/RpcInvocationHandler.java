package com.evoluc.asyni.rpc;


import com.evoluc.asyni.common.entity.RpcRequest;
import com.evoluc.asyni.rpc.client.RequestPromise;
import com.evoluc.asyni.rpc.client.RpcClientProcessor;
import com.evoluc.asyni.rpc.client.RpcClientTransport;
import com.evoluc.asyni.util.SnowflakeIdWorker;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Data
public class RpcInvocationHandler<T> implements InvocationHandler {

    private RpcClientTransport clientTransport;

    private RequestPromise<T> promise;

    public RpcInvocationHandler (RpcClientTransport clientTransport, RequestPromise<T> promise) {
        this.clientTransport = clientTransport;
        this.promise = promise;
    }

    @Override
    public Object invoke (Object proxy, Method method, Object[] args) throws Throwable {

        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }

        RpcRequest request = RpcRequest.builder()
                .className(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .argTypes(method.getParameterTypes())
                .args(args)
                .id(SnowflakeIdWorker.defaultNextId())
                .build();

        RpcClientProcessor clientProcessor = clientTransport.getClientProcessor();

        clientProcessor.synchRespMap.put(request.getId(), promise.getFuture());

        clientTransport.send(request);

        return promise.getFuture().get();
    }


}
