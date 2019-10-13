package com.evoluc.asyni.common;

import com.evoluc.asyni.util.ProtostuffSerializingUtil;

public class ProtostuffSerializer implements Serializer{

    public ProtostuffSerializer () {
    }

    @Override
    public <T> byte[] serialize (T obj) {
        return ProtostuffSerializingUtil.serializeObject(obj);
    }

    @Override
    public <T> T deserialize (byte[] bs, Class<T> klass) {
        return ProtostuffSerializingUtil.deserialize(bs,klass);
    }
}
