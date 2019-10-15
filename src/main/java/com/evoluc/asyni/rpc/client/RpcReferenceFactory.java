package com.evoluc.asyni.rpc.client;

import com.evoluc.asyni.common.annotation.RpcReference;
import com.evoluc.asyni.common.exception.RpcException;
import com.evoluc.asyni.rpc.RpcDiscoverer;
import com.evoluc.asyni.rpc.RpcInvocationHandler;
import com.evoluc.asyni.util.ClassUtils;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class RpcReferenceFactory {

    private static final Map<Pair, Object> PROXIES = new HashMap<>();
    private final ClientConfig config;
    private final RpcDiscoverer discoverer;
    private volatile boolean state;

    public RpcReferenceFactory(ClientConfig config, RpcDiscoverer discoverer) {
        this.config = config;
        this.discoverer = discoverer;
    }


    public <T> T wrap(Class<T> referenceType) throws RpcException {

        if (referenceType == null){
            throw new NullPointerException("[referenceType]不能为空");
        }
        if (!referenceType.isInterface()) {
            throw new RpcException(String.format("[%s]不是接口类型", referenceType));
        }
        RpcReference rpcService = ClassUtils.getAnnotation(referenceType, RpcReference.class);

        if (rpcService != null) return wrap(referenceType, rpcService.name());

        return wrap(referenceType, "");
    }

    public <T> T wrap(Class<T> referenceType, String name) throws RpcException{
        Pair pair = new Pair(referenceType, name);

        Object bean = PROXIES.get(pair);

        if (bean != null) {
            return referenceType.cast(bean);
        }

        RpcClientTransport clientTransport = discoverer.init(config).load(
                new InetSocketAddress(config.getHost(), config.getPort()));

        RpcInvocationHandler invocationHandler = new RpcInvocationHandler(clientTransport, config, name);

        T obj = referenceType.cast(Proxy
                .newProxyInstance(
                        referenceType.getClassLoader(),
                        new Class[]{referenceType},
                        invocationHandler
                ));

        PROXIES.put(pair, obj);
        return obj;

    }

//    /**
//     * 启动
//     *
//     * @throws IOException io异常时抛出
//     */
//    public void start() throws IOException {
//        if (state) {
//            return;
//        }
//        state = true;
//        if (discoverer != null) {
//        }
//    }

    /**
     * 停止
     */
    public void stop() {
        if (!state) {
            return;
        }
        state = false;
        PROXIES.clear();
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    private static class Pair {

        final Class<?> clazz;
        final String name;

    }

}
