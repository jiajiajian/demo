package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 车辆监控查询条件
 * @author tiza
 */
@Getter
@Setter
public class VehicleMonitorQuery extends Query {
    /**
     * 组织ID
     */
    private Long organizationId;
    /**
     * 车辆类型
     */
    private Long vehicleTypeId;
    /**
     * 车辆型号
     */
    private Long vehicleModelId;
    /**
     * acc状态
     */
    private Integer acc;
    /**
     * 锁车状态
     */
    private Integer lock;
    /**
     * 定位状态
     */
    private Integer gps;
    /**
     * 销售状态
     */
    private Integer saleStatus;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * vin码
     */
    private String vin;
    /**
     * 数据更新时间
     */
    private Long dataUpdateTime;
    /**
     * 终端状态
     */
    private Integer online;
    /**
     * 天数
     */
    private Long days;

    private Integer status;

    private Integer faultStatus;

    private List<String> vinList;
    @Override
    protected void convertParams() {
        add("organizationId", this.organizationId);
        add("vehicleTypeId", this.vehicleTypeId);
        add("vehicleModelId", this.vehicleModelId);
        add("acc", this.acc);
        add("lock", this.lock);
        add("gps", this.gps);
        add("saleStatus", this.saleStatus);
        add("province", this.province);
        add("city", this.city);
        add("vin", this.vin,true);
        add("dataUpdateTime", this.dataUpdateTime);
        add("online", this.online);
        add("faultStatus", this.faultStatus);
        add("days", this.days);
        add("vinList", this.vinList);
    }
}
