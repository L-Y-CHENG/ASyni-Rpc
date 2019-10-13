package com.evoluc.asyni.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * protostuff 序列化与反序列化工具
 */
public class ProtostuffSerializingUtil {


    static Map<Class, Schema> schemaCache = new ConcurrentHashMap<>();

    /**
     * common protostuff serialize, object need a empty constructor
     * Be careful to convert result byte[] to String, use new String(bytes, StandardCharsets.UTF_16LE).
     *
     * @param obj 序列化对象
     * @param <T> 泛型
     * @return byte[]
     */
    public static <T> byte[] serializeObject(T obj) {
        Class<T> klass = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(4096);
        try {
            if (schemaCache.containsKey(klass)) {
                return ProtostuffIOUtil.toByteArray(obj, schemaCache.get(klass), buffer);
            } else {
                schemaCache.put(klass, RuntimeSchema.getSchema(klass));
                return ProtostuffIOUtil.toByteArray(obj, schemaCache.get(klass), buffer);
            }
        } finally {
            buffer.clear();
        }
    }

    /**
     * common protostuff unserialize
     *
     * @param bs 反序列化Byte数组
     * @param klass 类型
     * @param <T> 泛型
     * @return  T
     */
    public static <T> T deserialize(byte[] bs, Class<T> klass) {
        if (schemaCache.containsKey(klass)) {
            Schema<T> schema = schemaCache.get(klass);
            T msg = schema.newMessage();
            ProtostuffIOUtil.mergeFrom(bs, msg, schema);
            return msg;
        } else {
            Schema<T> schema = RuntimeSchema.getSchema(klass);
            T msg = schema.newMessage();
            schemaCache.put(klass, schema);
            ProtostuffIOUtil.mergeFrom(bs, msg, schema);
            return msg;
        }
    }
}
