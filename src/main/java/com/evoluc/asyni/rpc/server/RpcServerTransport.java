package com.evoluc.asyni.rpc.server;

import com.evoluc.asyni.common.entity.RpcRequestKey;
import com.evoluc.asyni.common.entity.RpcServerBody;
import com.evoluc.asyni.rpc.RpcProtocol;
import lombok.Getter;
import org.smartboot.socket.transport.AioQuickServer;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class RpcServerTransport {

    private AioQuickServer<byte[]> server;

    @Getter
    private RpcServerProcessor rpcServerProcessor;

    {
        rpcServerProcessor = new RpcServerProcessor();
    }

    public void start(InetSocketAddress address) throws IOException {
        server = new AioQuickServer<>(address.getPort(), new RpcProtocol(), rpcServerProcessor);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }


    public void register (RpcRequestKey key, RpcServerBody body){
        rpcServerProcessor.addService(key, body);
    }

    public void clear(){
        rpcServerProcessor.clear();
    }


}
