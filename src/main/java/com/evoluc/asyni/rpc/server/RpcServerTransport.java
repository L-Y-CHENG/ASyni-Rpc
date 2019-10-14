package com.evoluc.asyni.rpc.server;

import com.evoluc.asyni.rpc.RpcProtocol;
import org.smartboot.socket.transport.AioQuickServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class RpcServerTransport {

    private AioQuickServer<byte[]> server;
    private RpcServerProcessor rpcServerProcessor = new RpcServerProcessor();

    public void start(InetSocketAddress address) throws IOException {
        server = new AioQuickServer<>(address.getPort(), new RpcProtocol(), rpcServerProcessor);
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }



}
