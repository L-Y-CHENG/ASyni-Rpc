package com.evoluc.asyni.rpc;

import org.smartboot.socket.transport.AioQuickClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public class RpcClientTransport {

    private AioQuickClient client;
    private RpcClientProcessor clientProcessor = new RpcClientProcessor();


    public void connect(InetSocketAddress address) throws IOException, ExecutionException, InterruptedException {
        client = new AioQuickClient<>(address.getHostName(), address.getPort(), new RpcProtocol(), clientProcessor);
        client.start();
    }

    public void close() {
        if (client != null) {
            client.shutdown();
        }
    }

}
