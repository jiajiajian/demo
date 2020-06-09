package cn.com.tiza.web.vm;

import lombok.Data;

/**
 * @author villas
 */
@Data
public class MessageVM {

    private String vin;

    private String terminalCode;

    private String sim;

    private String protocolType;

    private String cmdType;

    private Integer bodyLength;

    private String collectTime;

    private String body;
}
