package cn.com.tiza.web.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Data
public class CmdLogVM {

    private Long id;
    /**
     * 指令编号，10进制
     */
    private Integer cmd;
    /**
     * 执行状态：0：成功  1：失败  2：超时  3：执行中  4：离线指令已发送
     */
    private Integer runState;
    /**
     * 平台返回的check_id
     */
    private String runStateName;
    private String loginName;
    private Long runTime;
    private String vin;
    private String cmdName;

    public CmdLogVM() {
    }


}
