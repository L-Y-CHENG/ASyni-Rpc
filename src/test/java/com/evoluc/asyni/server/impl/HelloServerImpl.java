package com.evoluc.asyni.server.impl;

import com.evoluc.asyni.server.HelloServer;

public class HelloServerImpl implements HelloServer {

    @Override
    public String hello (String s) {
        return "hello " + s;
    }
}
