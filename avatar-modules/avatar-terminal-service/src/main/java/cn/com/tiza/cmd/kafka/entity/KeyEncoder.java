package cn.com.tiza.cmd.kafka.entity;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.Charset;
import java.util.Map;

public class KeyEncoder implements Serializer<String> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String topic, String key) {
        return key.getBytes(Charset.forName("UTF-8"));
    }

    @Override
    public void close() {
    }
}
