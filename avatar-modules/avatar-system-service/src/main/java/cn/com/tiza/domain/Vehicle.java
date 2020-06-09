package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
@Table(name = "v_vehicle")
public class Vehicle implements Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 按揭年限
     */
    private Integer loanPeriod;
    /**
     * 注册时间yyyyMMdd
     */
    private Integer registDate;

    private Long saleDate;
    /**
     * 销售方式
     */
    private Integer saleMethod;
    /**
     * 销售状态
     */
    private Integer saleStatus;
    /**
     * 销售员
     */
    private String seller;
    /**
     * 服务结束时间yyyyMMdd
     */
    private Integer serviceEndDate;
    /**
     * 服务期限
     */
    private Integer servicePeriod;
    /**
     * 服务开始时间yyyyMMdd
     */
    private Integer serviceStartDate;
    /**
     * 服务状态
     */
   // private ServiceStatus serviceStatus;
    /**
     * 合同号
     */
    private String contractNumber;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 创建用户登录名
     */
    private String createUserAccount;
    /**
     * 创建用户姓名
     */
    private String createUserRealname;
    /**
     * 客户ID
     */
    private Long customerId;
    /**
     * 备注
     */
    private String description;
    /**
     * 融资机构ID
     */
    private Long financeId;
    /**
     * 机构ID
     */
    private Long organizationId;
    /**
     * 终端id
     */
    private Long terminalId;
    /**
     * 更新时间
     */
    private Long updateTime;
    /**
     * 更新用户登录名
     */
    private String updateUserAccount;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname;
    /**
     * 车型ID
     */
    private Long vehicleModelId;
    /**
     * 车辆类型
     */
    private Long vehicleTypeId;
    /**
     * 机器序列号
     */
    private String vin;

    public Vehicle() {
    }

}
