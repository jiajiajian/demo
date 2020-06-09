package cn.com.tiza.dto;

import lombok.Data;

/**
 * @author tiza
 */
@Data
public class Message {

    private AlarmType alarmType;

    /**
     * 1 = 增加
     * -1 = 减少
     */
    private Integer amount = 1;

    public Message(){

    }

    public Message(AlarmType alarmType, Integer amount) {
        this.alarmType = alarmType;
        this.amount = amount;
    }
}
