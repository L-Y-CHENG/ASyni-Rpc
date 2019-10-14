package com.evoluc.asyni.common.config;

import lombok.Data;

@Data
public class ServerConfig {

    /**
     * 超时时间，单位秒
     */
    private int timeout = 5;

    /**
     * 注册地址
     */
    private String registryAddress;

    /**
     * 绑定主机
     */
    private final String host;
    /**
     * 绑定端口
     */
    private final int port;

    public ServerConfig (int port) {
        this("127.0.0.1", port);
    }

    public ServerConfig (String host, int port) {
        this.host = host;
        this.port = port;
    }
    
}
