package cn.com.tiza.service.dto;

import lombok.Data;


/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
public class LockDto {

    /**
     * 远程锁车id
     */
    private Long id;
    /**
     * 执行状态：0：失败  1：成功  2：超时  3：执行中  4：离线指令已发送
     */
    private Integer runState;
    /**
     * 执行指令---对应数据库字典的id
     */
    private Long dicItemId;
    /**
     * 执行人
     */
    private String loginName;
    /**
     * 执行时间
     */
    private Long time;
    /**
     * 机器序列号
     */
    private String vin;

    public LockDto() {
    }


}
