package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import cn.com.tiza.util.LocalDateTimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Getter
@Setter
public class VehicleQuery extends Query {

    private Long selectOrg;
    private Long vehicleTypeId;
    private Long vehicleModelId;
    private Integer saleStatus;
    private Integer serviceStatus;
    private String code;
    private Integer serviceStartDate1;
    private Long serviceStartMill1;
    private Integer serviceStartDate2;
    private Long serviceStartMill2;
    private Integer serviceEndDate1;
    private Integer serviceEndDate2;
    private Long serviceEndMill1;
    private Long serviceEndMill2;
    /**
     * 批量查询vinList
     */
    private List<String> vinList;

    private boolean location;

    @Override
    protected void convertParams() {
        add("selectOrg", this.selectOrg);
        add("vehicleTypeId", this.vehicleTypeId);
        add("vehicleModelId", this.vehicleModelId);
        add("saleStatus", this.saleStatus);
        add("serviceStatus", this.serviceStatus);
        add("code", this.code, true);
        add("serviceStartDate1", this.serviceStartDate1);
        add("serviceStartDate2", this.serviceStartDate2);
        add("serviceEndDate1", this.serviceEndDate1);
        add("serviceEndDate2", this.serviceEndDate2);
        add("vinList", this.vinList);
        add("location", this.location);
    }

    public void formatServiceDateMil() {
        if (Objects.nonNull(serviceStartMill1)) {
            String formatDay = LocalDateTimeUtils.formatDay(serviceStartMill1, "yyyyMMdd");
            this.serviceStartDate1 = Integer.parseInt(formatDay);
        }
        if (Objects.nonNull(serviceStartMill2)) {
            String formatDay = LocalDateTimeUtils.formatDay(serviceStartMill2, "yyyyMMdd");
            this.serviceStartDate2 = Integer.parseInt(formatDay);
        }
        if (Objects.nonNull(serviceEndMill1)) {
            String formatDay = LocalDateTimeUtils.formatDay(serviceEndMill1, "yyyyMMdd");
            this.serviceEndDate1 = Integer.parseInt(formatDay);
        }
        if (Objects.nonNull(serviceEndMill2)) {
            String formatDay = LocalDateTimeUtils.formatDay(serviceEndMill2, "yyyyMMdd");
            this.serviceEndDate2 = Integer.parseInt(formatDay);
        }

    }

}
