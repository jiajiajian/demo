package cn.com.tiza.service;

import cn.com.tiza.service.dto.KafkaAlarmDto;

/**
 * kafka消息消费
 *
 * @author tz0920
 */
public interface AlarmService {
    /**
     * 消费报警数据
     *
     * @param kafkaAlarmDto
     */
    void consume(KafkaAlarmDto kafkaAlarmDto);
}
