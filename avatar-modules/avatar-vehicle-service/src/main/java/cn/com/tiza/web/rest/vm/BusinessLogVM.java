package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-04-16
 *
 * @author tiza
 */
@Data
public class BusinessLogVM {

    private Long id;
    /**
     * 操作类型 1车辆删除 2移机 3销户 4批量延长服务期 5更换终端 6暂停 7暂停恢复
     */
    private Integer operateType;
    private Long createTime;
    private String createUserAccount;
    private String createUserRealname;
    /**
     * 旧的SIM卡号
     */
    private String oldSimcard;
    /**
     * 旧的终端编号
     */
    private String oldTerminal;
    /**
     * 旧VIN
     */
    private String oldVin;
    private Long organizationId;
    /**
     * 新的SIM卡号
     */
    private String simcard;
    /**
     * 新的终端编号
     */
    private String terminal;
    /**
     * 当前VIN
     */
    private String vin;
    private String orgName;
    /**
     * 注册时间
     */
    private Long registDate;
    private Integer serviceEndDate;
    private Integer renewalMon;

    public BusinessLogVM() {
    }


}
