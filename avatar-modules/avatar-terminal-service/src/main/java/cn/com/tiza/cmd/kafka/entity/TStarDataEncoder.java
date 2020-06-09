package cn.com.tiza.cmd.kafka.entity;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * TStarData Kafka encoder
 *
 * @author TZ0643
 */
public class TStarDataEncoder implements Serializer<TStarData> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public byte[] serialize(String s, TStarData data) {
        return data.toBytes();
    }

    @Override
    public void close() {
    }
}
