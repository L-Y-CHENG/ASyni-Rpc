package com.evoluc.asyni.rpc;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;

/**
 * 消息解码 根据长度解码
 */
public class RpcProtocol implements Protocol<byte[]> {

    private static final int INTEGER_BYTES = Integer.SIZE / Byte.SIZE;

    @Override
    public byte[] decode(ByteBuffer readBuffer, AioSession<byte[]> session) {
        int remaining = readBuffer.remaining();
        if (remaining < INTEGER_BYTES) {
            return null;
        }
        int messageSize = readBuffer.getInt(readBuffer.position());
        if (messageSize > remaining) {
            return null;
        }
        byte[] data = new byte[messageSize - INTEGER_BYTES];
        readBuffer.getInt();
        readBuffer.get(data);
        return data;
    }

}
