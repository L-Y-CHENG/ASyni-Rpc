package com.evoluc.asyni;

import com.evoluc.asyni.proto.TestMessage;
import com.evoluc.asyni.util.SerializingUtil;
import com.google.common.primitives.Bytes;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.Assert;
import org.junit.Test;


public class TestProtoBuf {

    @Test
    public void testProtoBuf () throws InvalidProtocolBufferException {
//        final TestMessage.ReqProto.Builder builder = TestMessage.ReqProto.newBuilder();
//        builder.setUid(1L).setText("I Want My Tears Back").setType(3);
//
//        TestMessage.ReqProto reqProto = builder.build();
//
//        System.out.println(">>>>start>>>>> : " + reqProto);
//        System.out.println("getSerializedSize : "+reqProto.getSerializedSize());
//        System.out.println(reqProto.getSerializedSize());
////        Stream.of(reqProto.toByteArray()).forEach(System.out::println);
//        byte[] bytes = reqProto.toByteArray();
//        for (byte b : bytes){
//            System.out.print(b);
//        }
//        System.out.println(">>>>>>>>>>>>>");
//        TestMessage.ReqProto formProto = TestMessage.ReqProto.parseFrom(bytes);
//        System.out.println("after Uid:" + formProto.getUid());
//        System.out.println("after Text:" + formProto.getText());
//        System.out.println("after Type:" + formProto.getType());
    }
    
    class test {
        String t;
        Integer i;
    }


    @Test
    public void testProtoStuff() {
        String expect = "hello, world.";
        byte[] serialized = SerializingUtil.serialize(expect);
        Assert.assertEquals(SerializingUtil.deserialize(serialized, String.class), expect);
    }




}
