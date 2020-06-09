package cn.com.tiza.dto;

import lombok.Data;

import java.util.Map;

@Data
public class CommandMessage {

    private Integer cmdId;

    private Integer state;

    private Map<String, Object> result;

    public CommandMessage() {
    }

    public CommandMessage(Integer cmdId, Integer state) {
        this.cmdId = cmdId;
        this.state = state;
    }
}
