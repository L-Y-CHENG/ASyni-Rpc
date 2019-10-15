package com.evoluc.asyni;

import com.evoluc.asyni.rpc.server.RpcServiceFactory;
import com.evoluc.asyni.rpc.server.ServerConfig;
import com.evoluc.asyni.server.impl.HelloServerImpl;

import java.io.IOException;


public class Provider {

    public static void main(String[] args) throws IOException {
        ServerConfig config = new ServerConfig(8080);
        RpcServiceFactory serviceFactory = new RpcServiceFactory(config);
        serviceFactory.register(new HelloServerImpl());
        serviceFactory.server();
    }
}
