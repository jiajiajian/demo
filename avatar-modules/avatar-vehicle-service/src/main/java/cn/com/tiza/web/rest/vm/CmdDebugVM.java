package cn.com.tiza.web.rest.vm;


import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class CmdDebugVM {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 配置指令
     */
    private String cmd;
    private List<HashMap<String,String>> cmdList;
    private int cmdNum;
    /**
     * 机构ID
     */
    private Long organizationId;
    private String orgName;
    /**
     * 车型ID
     */
    private Long vehicleTypeId;
    private String vehicleTypeName;
    private Long updateTime;
    private String updateUserAccount;

    public CmdDebugVM() {
    }


}
