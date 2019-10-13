package com.evoluc.asyni;

import com.evoluc.asyni.util.ProtostuffSerializingUtil;
import org.junit.Test;

public class TestProtoBuf {

    @Test
    public void testProtoBuf () {
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

    @Test
    public void testProtoStuff() {
        TestParams testParams = TestParams.builder().testInt(2).testString(">>>>>>>>>>>>>").build();
        byte[] serialized = ProtostuffSerializingUtil.serializeObject(testParams);
        TestParams testParams1 = ProtostuffSerializingUtil.deserialize(serialized, TestParams.class);
        System.out.println(serialized.length);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        for (byte b : serialized){
            System.out.print("{"+b+"} ");
        }
        System.out.println();
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(testParams1.getTestInt());
        System.out.println(testParams1.getTestString());
    }




}
