package com.evoluc.asyni.common.config;

import lombok.Data;

@Data
public class ClientConfig {

    /**
     * 超时时间，单位秒
     */
    private int timeout = 5;

    /**
     * 注册地址
     */
    private String address;

    /**
     * 空闲超时时间，单位秒
     */
    private int idleTimeout = 120;

    /**
     * 是否异步请求
     */
    private boolean async;

}
