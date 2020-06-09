package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * gen by beetlsql 2020-03-21
 * @author tiza
 */
@Getter
@Setter
public class RemoteLockCarLogQuery extends Query {
    /**
     * 车辆类型
     */
    private Long vehicleTypeId;
    /**
     * 车辆型号
     */
    private Long vehicleModelId;
    /**
     * 操作类型
     */
    private Integer order;
    /**
     * 操作结果
     */
    private Integer status;
    /**
     * 机器序列号/终端编号/SIM卡号
     */
    private String keyword;
    /**
     * 开始时间
     */
    private Long startDate;
    /**
     * 结束时间
     */
    private Long endDate;

    @Override
    protected void convertParams() {
        add("vehicleTypeId", this.vehicleTypeId);
        add("vehicleModelId", this.vehicleModelId);
        add("order", this.order);
        add("status", this.status);
        add("keyword", this.keyword, true);
        add("startDate", this.startDate, true);
        add("endDate", this.endDate, true);

    }

}
