package com.evoluc.asyni.rpc.client;

import com.evoluc.asyni.common.annotation.RpcService;
import com.evoluc.asyni.common.config.ClientConfig;
import com.evoluc.asyni.common.exception.RpcException;
import com.evoluc.asyni.rpc.RpcDiscoverer;
import com.evoluc.asyni.rpc.RpcInvocationHandler;
import com.evoluc.asyni.util.ClassUtils;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class RpcReferenceFactory {

    private static final Map<Class<?>, Object> PROXIES = new HashMap<>();
    private final ClientConfig config;
    private final RpcDiscoverer discoverer;
    private volatile boolean state;

    public RpcReferenceFactory(ClientConfig config, RpcDiscoverer discoverer) {
        this.config = config;
        this.discoverer = discoverer;
    }


    public <T> T create(Class<T> referenceType) throws RpcException {
        if (referenceType == null){throw new NullPointerException("[referenceType]不能为空"); }
        if (!referenceType.isInterface()) {
            throw new RpcException(String.format("[%s]不是接口类型", referenceType));
        }

        RpcService rpcService = ClassUtils.getAnnotation(referenceType, RpcService.class);
        Object bean = PROXIES.get(referenceType);
        if (bean != null) {
            return referenceType.cast(bean);
        }

        RpcClientTransport clientTransport = discoverer.load();
        RequestPromise<T> requestPromise = new RequestPromise<>(new CompletableFuture<>());

        RpcInvocationHandler<T> invocationHandler = new RpcInvocationHandler<T>(clientTransport, requestPromise);


        T obj = referenceType.cast(Proxy
                .newProxyInstance(
                        referenceType.getClassLoader(),
                        new Class[]{referenceType},
                        invocationHandler
                ));

        PROXIES.put(referenceType, obj);
        return obj;
    }

}
