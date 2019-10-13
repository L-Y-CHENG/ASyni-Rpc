package com.evoluc.asyni.rpc;

import org.smartboot.socket.transport.AioQuickServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RpcServerTransport {

    private  AioQuickServer<byte[]> server;

    public void start(InetSocketAddress address) throws IOException {
        RpcServerProcessor rpcServerProcessor = new RpcServerProcessor();
        server = new AioQuickServer<>(address.getPort(), new RpcProtocol(), rpcServerProcessor);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

}
