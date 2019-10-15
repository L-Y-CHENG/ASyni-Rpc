package com.evoluc.asyni.rpc;

import com.evoluc.asyni.rpc.client.ClientConfig;
import com.evoluc.asyni.common.exception.RpcException;
import com.evoluc.asyni.rpc.client.RpcClientTransport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RpcDiscoverer {

    private static final Random RANDOM = new Random();
    private ExpiringMap<InetSocketAddress, RpcClientTransport> transports;
    @Getter
    private boolean init;

    public RpcDiscoverer init(ClientConfig config) {
        this.transports = ExpiringMap.builder()
                .expiration(config.getIdleTimeout(), TimeUnit.SECONDS)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .asyncExpirationListener(this::expired).build();
        this.init = true;
        return this;
    }

    public RpcClientTransport load(List<InetSocketAddress> addresses) throws RpcException {
        if (!init) throw new RpcException("RpcDiscoverer 尚未初始化");
        int size = addresses.size();
        int index = RANDOM.nextInt(size);
        for (int i = 0; i < size; i++) {
            InetSocketAddress address = addresses.get(index);
            RpcClientTransport transport = get(address);
            index = (index + 1) % addresses.size();
            if (transport != null) {
                return transport;
            }
        }
        throw new RpcException("远程服务不可用");
    }

    public RpcClientTransport load(InetSocketAddress address) throws RpcException{
        if (!init) throw new RpcException("RpcDiscoverer 尚未初始化");
        return get(address);
    }

    private synchronized RpcClientTransport get(InetSocketAddress address) {
        RpcClientTransport transport = transports.get(address);
        if (transport != null && transport.isRunning()) {
            return transport;
        }
        transport = new RpcClientTransport();
        try {
            transport.connect(address);
            transports.put(address, transport);
        } catch (Exception e) {
            log.warn("连接服务端 [{}] 失败", address, e);
        }
        return transport;
    }

    private void expired(InetSocketAddress address, RpcClientTransport transport) {
        if (log.isDebugEnabled()) {
            log.debug(" [{}] 空闲连接超时，关闭连接", address);
        }
        transport.close();
    }
}
