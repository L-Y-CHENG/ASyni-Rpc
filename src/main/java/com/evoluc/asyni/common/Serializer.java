package com.evoluc.asyni.common;

public interface Serializer {

    <T> byte[] serialize(T obj);

    <T> T deserialize(byte[] bs, Class<T> klass);
}
