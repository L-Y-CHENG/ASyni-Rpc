package com.evoluc.asyni;

import com.evoluc.asyni.common.exception.RpcException;
import com.evoluc.asyni.rpc.RpcDiscoverer;
import com.evoluc.asyni.rpc.client.ClientConfig;
import com.evoluc.asyni.rpc.client.RpcReferenceFactory;
import com.evoluc.asyni.server.HelloServer;
import org.junit.Test;

public class Consumer {

    @Test
    public void consumer() throws RpcException {
        ClientConfig config = new ClientConfig("127.0.0.1", 8080);
        RpcDiscoverer discoverer = new RpcDiscoverer();
        RpcReferenceFactory factory = new RpcReferenceFactory(config, discoverer.init(config));

        HelloServer helloServer = factory.wrap(HelloServer.class);
        String s = helloServer.hello("world");
        System.out.println(s);
    }


}
