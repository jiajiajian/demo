package cn.com.tiza.service;

import cn.com.tiza.dto.AlarmType;
import cn.com.tiza.service.dto.KafkaAlarmDto;
import cn.com.tiza.tstar.common.entity.TStarData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author: TZ0920
 **/
@Slf4j
@Component
@ConditionalOnProperty(name = "application.kafka.bootstrapServers")
public class KafkaAlarmListener {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 消费kafka的消息
     *
     * @param record
     */
    @KafkaListener(topics = "#{'${application.kafka.listener_topics}'.split(',')}")
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        log.info("topic.quick.consumer receive {}", record.toString());
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            TStarData data = (TStarData) kafkaMessage.get();
            String json = new String(data.getMsgBody());
            log.debug("data from kafka {}", json);
            try {
                KafkaAlarmDto dto = objectMapper.readValue(json, KafkaAlarmDto.class);
                //获取消费报警实现策略
                AlarmService alarmService = AlarmConsumeStrategyFactory.getByAlarmType(AlarmType.fromValue(dto.getAlarmType()));
                if (Objects.nonNull(alarmService)) {
                    alarmService.consume(dto);
                }
                //手动提交offset
                ack.acknowledge();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
