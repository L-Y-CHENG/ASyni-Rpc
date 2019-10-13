package com.evoluc.asyni.rpc;

import com.evoluc.asyni.common.ProtostuffSerializer;
import com.evoluc.asyni.common.Serializer;
import com.evoluc.asyni.common.entity.RpcRequest;
import com.evoluc.asyni.common.entity.RpcResponse;
import org.smartboot.socket.MessageProcessor;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class RpcClientProcessor implements MessageProcessor<byte[]> {


    private Map<Long, CompletableFuture<RpcResponse>> synchRespMap = new ConcurrentHashMap<>();
    private Map<Class, Object> objectMap = new ConcurrentHashMap<>();
    private AioSession<byte[]> aioSession;

    private Serializer serializer = new ProtostuffSerializer();

    @Override
    public void process (AioSession<byte[]> aioSession, byte[] msg) {
        try {
            RpcResponse response =serializer.deserialize(msg, RpcResponse.class);
            synchRespMap.get(response.getId()).complete(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stateEvent (AioSession<byte[]> aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {

    }

    public final RpcResponse send(RpcRequest request) throws Exception {
        CompletableFuture<RpcResponse> rpcResponseCompletableFuture = new CompletableFuture<>();
        synchRespMap.put(request.getId(), rpcResponseCompletableFuture);

        //输出消息
        byte[] data = serializer.serialize(request);

        synchronized (aioSession) {
            aioSession.writeBuffer().writeInt(data.length + 4);
            aioSession.writeBuffer().write(data);
            aioSession.writeBuffer().flush();
        }
        try {
            return rpcResponseCompletableFuture.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new SocketTimeoutException("Message is timeout!");
        }
    }

}
