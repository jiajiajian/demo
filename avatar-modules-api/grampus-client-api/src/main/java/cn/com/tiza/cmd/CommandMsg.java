package cn.com.tiza.cmd;

import lombok.Data;

@Data
public class CommandMsg {
    /**
     * 指令id
     */
    private Integer cmdId;
    /**
     * 时间 eg：20200513
     */
    private Integer date;
    /**
     * 报文 eg：MTIzNDU2Nw==
     */
    private String msgBody;
    /**
     * 命令序列号
     */
    private Integer serialNo;
    /**
     * 状态 0：响应成功 7：指令已发送终端
     */
    private Integer state;
    /**
     * 时间 毫秒
     */
    private Long time;
    /**
     * VIN码
     */
    private String vin;

    /*--------------下面是非kafka中字段---------------------*/

    /**
     * 请求的次数
     */
    private int count = 1;
}
