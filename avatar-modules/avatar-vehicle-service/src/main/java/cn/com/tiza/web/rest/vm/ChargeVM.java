package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-04-20
 *
 * @author tiza
 */
@Data
public class ChargeVM {

    private Long id;
    /**
     * 配置状态 0:未配置 1:已配置
     */
    private Integer configFlag;
    /**
     * 组织
     */
    private String orgName;
    /**
     * 终端型号
     */
    private String terminalModel;

    public ChargeVM() {
    }


}
