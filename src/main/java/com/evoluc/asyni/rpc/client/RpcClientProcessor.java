package com.evoluc.asyni.rpc.client;

import com.evoluc.asyni.common.ProtostuffSerializer;
import com.evoluc.asyni.common.Serializer;
import com.evoluc.asyni.common.entity.RpcRequest;
import com.evoluc.asyni.common.entity.RpcResponse;
import lombok.Getter;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RpcClientProcessor implements MessageProcessor<byte[]> {

    @Getter
    private Map<Long, CompletableFuture<RpcResponse>> futureMap = new ConcurrentHashMap<>();

//    public Map<Class, Object> objectMap = new ConcurrentHashMap<>();

    private AioSession<byte[]> aioSession;

    private Serializer serializer = new ProtostuffSerializer();

    @Override
    public void process (AioSession<byte[]> aioSession, byte[] msg) {
        try {
            RpcResponse response =serializer.deserialize(msg, RpcResponse.class);
            futureMap.get(response.getId()).complete(response);
            futureMap.remove(response.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stateEvent (AioSession<byte[]> aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
        switch (stateMachineEnum) {
            case NEW_SESSION:
                this.aioSession = aioSession;
                break;
        }
    }

    final void send(RpcRequest request) throws Exception {

        //输出消息
        byte[] data = serializer.serialize(request);

        synchronized (aioSession) {
            aioSession.writeBuffer().writeInt(data.length + 4);
            aioSession.writeBuffer().write(data);
            aioSession.writeBuffer().flush();
        }
    }

}
