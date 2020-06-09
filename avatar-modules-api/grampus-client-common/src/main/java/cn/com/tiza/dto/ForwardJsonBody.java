package cn.com.tiza.dto;

import lombok.Data;

/**
 * 转发报文中的body部分
 *
 * @author villas
 */
@Data
public class ForwardJsonBody {
    /**
     * 采集时间
     */
    private Long ct;
    /**
     * 接收时间
     */
    private Long rt;
    /**
     * 转发时间
     */
    private Long ft;

    private String vin;
    /**
     * 217:平台补发; 218:应答失败; 219:超时失败
     */
    private Integer cmd;
    /**
     * 原始报文
     */
    private String body;
}