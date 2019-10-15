package com.evoluc.asyni.rpc.server;

import com.evoluc.asyni.common.annotation.RpcService;
import com.evoluc.asyni.common.entity.RpcRequestKey;
import com.evoluc.asyni.common.entity.RpcServerBody;
import com.evoluc.asyni.util.ClassUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

@Slf4j
public class RpcServiceFactory {

    private final ServerConfig serverConfig;
    private RpcServerTransport transport;
    private volatile boolean state;

    public RpcServiceFactory (ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        transport = new RpcServerTransport();
    }


    /**
     * 注册服务
     *
     * @param service 服务实现
     */
    public void register(Object service) {
        RpcService annotation = ClassUtils
                .getAnnotation(ClassUtils.getCglibActualClass(service.getClass()), RpcService.class);
        if (annotation != null) {
            register(service, annotation.name());
        } else {
            register(service, "");
        }
    }

    /**
     * 注册服务
     *
     * @param service 服务实现
     * @param version 服务版本
     */
    public void register(Object service, String version) {
        Class<?> serviceType = service.getClass();
        Class<?>[] interfaces = serviceType.getInterfaces();
        if (interfaces == null || interfaces.length == 0) {
            return;
        }
        for (Class<?> intf : interfaces) {
            if (intf.getClassLoader() == null && intf.getName().startsWith("java.")) {
                continue;
            }
            register(intf, service, version);
        }
    }


    /**
     * 注册服务，指定接口和版本
     *
     * @param interfaceType 接口类型
     * @param service 服务实现
     * @param name 服务名称
     */
    public void register(Class<?> interfaceType, Object service, String name){
        Method[] methods = interfaceType.getDeclaredMethods();
        if (methods.length == 0) {
            return;
        }
        Class<?> clazz = service.getClass();
        for (Method method : methods) {
            try {
                Method realMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                transport.register(
                        RpcRequestKey.builder()
                                .className(interfaceType.getName())
                                .methodName(method.getName())
                                .types(method.getParameterTypes())
                                .name(name)
                                .build(),
                        new RpcServerBody(service, realMethod));

            } catch (NoSuchMethodException e) {
                log.warn(e.getMessage());
            }
        }
    }

    /**
     * 启动
     *
     * @throws IOException io异常时抛出
     */
    public void server() throws IOException {
        if (state) {
            return;
        }
        state = true;
        transport.start(new InetSocketAddress("localhost", 8080));
    }


    /**
     * 停止
     */
    public void stop() {
        if (!state) {
            return;
        }
        state = false;
        transport.clear();
        transport.stop();
    }

}
