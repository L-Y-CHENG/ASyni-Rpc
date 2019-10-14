package com.evoluc.asyni.rpc.server;

import com.evoluc.asyni.common.ProtostuffSerializer;
import com.evoluc.asyni.common.Serializer;
import com.evoluc.asyni.common.entity.RpcRequest;
import com.evoluc.asyni.common.entity.RpcRequestKey;
import com.evoluc.asyni.common.entity.RpcResponse;
import com.evoluc.asyni.common.entity.RpcServerBody;
import lombok.extern.slf4j.Slf4j;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class RpcServerProcessor implements MessageProcessor<byte[]> {

    private ExecutorService pool = Executors.newCachedThreadPool();
    private Map<RpcRequestKey, RpcServerBody> relationMap = new HashMap<>();
    private Serializer serializer = new ProtostuffSerializer();

    @Override
    public void process (AioSession<byte[]> session, byte[] msg) {
        pool.execute(() -> {
            RpcRequest request = serializer.deserialize(msg, RpcRequest.class);
            RpcResponse response = new RpcResponse(request.getId());
            RpcServerBody serverBody = relationMap.get(new RpcRequestKey(request));
            if (serverBody == null){
                response.setException(new Exception("无相关服务"));
            }else {
                try {
                    response.setResult(invoke(serverBody ,request.getArgs()));
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                    response.setException(e);
                }
            }

            byte[] data = serializer.serialize(response);
            synchronized (session) {
                try {
                    session.writeBuffer().writeInt(data.length + 4);
                    session.writeBuffer().write(data);
                    session.writeBuffer().flush();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }

        });
    }

    @Override
    public void stateEvent (AioSession<byte[]> aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {

    }


    /**
     * 服务调用
     * @param args 参数
     * @return 结果
     * @throws InvocationTargetException 反射异常
     * @throws IllegalAccessException 反射异常
     */
    private Object invoke(RpcServerBody serverBody ,Object[] args) throws InvocationTargetException, IllegalAccessException {
        return serverBody.method().invoke(serverBody.service(), args);
    }

    /**
     * 添加服务
     * @param requestKey 服务方法Key
     * @param serverBody 服务信息
     */
    public final void addService(RpcRequestKey requestKey, RpcServerBody serverBody) {
        relationMap.put(requestKey, serverBody);
    }
}
