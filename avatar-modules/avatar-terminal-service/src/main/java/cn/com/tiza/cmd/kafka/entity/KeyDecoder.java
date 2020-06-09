package cn.com.tiza.cmd.kafka.entity;

import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.Charset;
import java.util.Map;

public class KeyDecoder implements Deserializer<String> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public String deserialize(String topic, byte[] bytes) {
        if (bytes == null)
            return null;
        return new String(bytes, Charset.forName("UTF-8"));
    }

    @Override
    public void close() {
    }
}
