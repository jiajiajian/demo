package cn.com.tiza.tstar.common.entity;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * TStarData kafka decoder
 *
 * @author TZ0643
 */
public class TStarDataDecoder implements Deserializer<TStarData> {

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public TStarData deserialize(String topic, byte[] bytes) {
        return TStarData.bytes2TStarData(bytes, 0, bytes.length);
    }

    @Override
    public void close() {
    }
}
