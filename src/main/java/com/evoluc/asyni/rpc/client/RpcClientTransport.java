package com.evoluc.asyni.rpc.client;

import com.evoluc.asyni.common.entity.RpcRequest;
import com.evoluc.asyni.common.exception.RpcException;
import com.evoluc.asyni.rpc.RpcProtocol;
import lombok.Getter;
import org.smartboot.socket.transport.AioQuickClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class RpcClientTransport {

    private AioQuickClient<byte[]> client;

    @Getter
    private RpcClientProcessor clientProcessor;

    @Getter
    private boolean running;

    {
        clientProcessor = new RpcClientProcessor();
        running = false;
    }


    public void connect(InetSocketAddress address) throws IOException, ExecutionException, InterruptedException {
        client = new AioQuickClient<>(address.getHostName(), address.getPort(), new RpcProtocol(), clientProcessor);
        client.start();
        running = true;
    }

    public void close() {
        if (client != null) {
            client.shutdown();
        }
    }

    public void send (RpcRequest request, RequestPromise<?> promise) throws RpcException {
        if (!running) throw new RpcException("客户端未开启");
        try {
            clientProcessor.getFutureMap().put(request.getId(), promise.getResponseFuture());
            clientProcessor.send(request);
        }catch (Exception e){
            e.printStackTrace();
            throw new RpcException(e.getMessage());
        }
    }

}
