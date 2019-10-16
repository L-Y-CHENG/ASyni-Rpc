package com.evoluc.asyni.server.impl;

import com.evoluc.asyni.common.annotation.RpcService;
import com.evoluc.asyni.server.HelloServer;

@RpcService(name = "two")
public class HelloServerImpl2 implements HelloServer {

    @Override
    public String hello (String s) {
        return "hello2 :"+s;
    }
}
