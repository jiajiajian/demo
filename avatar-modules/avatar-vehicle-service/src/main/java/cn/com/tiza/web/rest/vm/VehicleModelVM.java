package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class VehicleModelVM {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 其他
     */
    private String description;
    /**
     * 类型名称
     */
    private String name;
    private Integer tonnage;
    /**
     * 机构ID
     */
    private Long organizationId;
    private String orgName;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 更新用户登录名
     */
    private String updateUserAccount;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname;
    /**
     * 类型ID
     */
    private Long vehicleTypeId;
    private String vehicleTypeName;

    public VehicleModelVM() {
    }

    public String[] toRow() {
        String[] arr = new String[6];
        int idx = 0;
        arr[idx++] = name;
        arr[idx++] = vehicleTypeName;
        arr[idx++] = tonnage + "T";
        arr[idx++] = orgName;
        arr[idx++] = updateUserAccount;
        arr[idx++] = updateTime;
        return arr;
    }

}
