package com.evoluc.asyni.rpc.client;

import lombok.Data;


@Data
public class ClientConfig {

    /**
     * 超时时间，单位秒
     */
    private int timeout = 5;

    /**
     * 空闲超时时间，单位秒
     */
    private int idleTimeout = 120;

    /**
     * 绑定主机
     */
    private final String host;
    /**
     * 绑定端口
     */
    private final int port;


    public ClientConfig (String host, int port) {
        this.host = host;
        this.port = port;
    }
}
