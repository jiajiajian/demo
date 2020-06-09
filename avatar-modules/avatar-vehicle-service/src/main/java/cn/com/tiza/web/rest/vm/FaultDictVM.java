package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class FaultDictVM {
    private Long id;
    /**
     * 故障字典文件
     */
    private String fileName;
    /**
     * 故障字典名称
     */
    private String name;
    /**
     * 机构ID
     */
    private Long organizationId;
    private String orgName;

    private Long updateTime;

    private String updateUserAccount;

    private String updateUserRealname;

    public FaultDictVM() {
    }


}
