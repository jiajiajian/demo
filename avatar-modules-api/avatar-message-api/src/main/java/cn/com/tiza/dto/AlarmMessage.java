package cn.com.tiza.dto;

import lombok.Data;

import java.util.List;

/**
 * 报警
 * @author tiza
 */
@Data
public class AlarmMessage {

    private Message message;

    private List<Long> userIds;
}
