package cn.com.tiza.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author TZ0781
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class NoticeDTO {

    /**
     * 通知时间-为空则立即发送，非空时需要定时发送
     */
    Long sendTime;

    /**
     * 故障/报警 id
     */
    Long alarmId;

    Long alarmTime;

    String noticeContent;
    String alarmItemName;
    String spn;
    String fmi;
    String tla;
    String vin;

    String subject;
    String templateParas;

    /**
     * 接收方： 手机号码/邮箱/手机DeviceId
     */
    List<String> receivers;
    String publishName;
    String content;
    AlarmType alarmType;
    Integer fenceType;
    String address;
}
