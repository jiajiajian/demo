package cn.com.tiza.protocol;

import lombok.Data;

import java.util.Map;

@Data
public class CmdBody {

    private Integer state;

    private byte[] body;

    private Map<String, Object> successMap;

    public CmdBody(Integer state) {
        this.state = state;
    }
}
