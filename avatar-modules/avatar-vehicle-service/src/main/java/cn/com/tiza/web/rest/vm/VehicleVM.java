package cn.com.tiza.web.rest.vm;


import cn.com.tiza.service.dto.LockItem;
import cn.com.tiza.service.dto.ServiceStatus;
import com.google.common.collect.Lists;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class VehicleVM extends LockVM {
    private Long id;
    /**
     * 机器序列号
     */
    private String vin;

    /**
     * 终端
     */
    private String terminalCode;
    /**
     * sim卡
     */
    private String simCard;
    private String orgName;
    private String financeName;
    private Long financeId;
    /**
     * 车型ID
     */
    private Long vehicleModelId;
    private String vehicleModel;
    /**
     * 车辆类型
     */
    private Long vehicleTypeId;
    private String vehicleType;
    private BigDecimal totalWorkTime;
    private Integer acc;
    private Integer gps;
    private String dataUpdateTime;
    private String gpsTime;
    private String gpsAddress;


    private Integer serviceStatus;
    private String serviceStartDate;
    private String serviceEndDate;
    private Integer servicePeriod;
    private String contractNumber;
    private String customerName;
    private Long customerId;
    private String alarmName;
    private String alarmNumber;
    private String phoneNumber;
    private Integer debugStatus;
    private String debugBeginTime;
    private String debugEndTime;
    private String registDate;
    private String serviceDate;
    private String debugDate;

    /**
     * 按揭年限
     */
    private Integer loanPeriod;
    /**
     * 销售日期时间戳
     */
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
    private Long organizationId;

    private BigDecimal lon;
    private BigDecimal lat;

    public VehicleVM() {
    }

    public String[] toRow() {
        String[] arr = new String[28];
        int idx = 0;
        arr[idx] = vin;
        arr[++idx] = terminalCode;
        arr[++idx] = simCard;
        arr[++idx] = orgName;
        arr[++idx] = financeName;
        arr[++idx] = vehicleType;
        arr[++idx] = vehicleModel;
        arr[++idx] = "" + totalWorkTime;
        arr[++idx] = formatAcc(acc);
        arr[++idx] = formatGps(acc);
        arr[++idx] = dataUpdateTime;
        arr[++idx] = gpsTime;
        arr[++idx] = gpsAddress;
        arr[++idx] = formatLock(super.getLockStatus());
        arr[++idx] = serviceStatus == null ? "" : ServiceStatus.fromValue(serviceStatus).getName();
        arr[++idx] = serviceStartDate;
        arr[++idx] = serviceEndDate;
        arr[++idx] = "" + servicePeriod;
        arr[++idx] = formatSaleStatus(saleStatus);
        arr[++idx] = contractNumber;
        arr[++idx] = customerName;
        arr[++idx] = contractNumber;
        arr[++idx] = alarmName;
        arr[++idx] = alarmNumber;
        arr[++idx] = formatDebugStatus(debugStatus);
        arr[++idx] = debugBeginTime;
        arr[++idx] = debugEndTime;
        arr[++idx] = registDate;
        return arr;
    }

    public String[] toRow1() {
        String[] arr = new String[27];
        int idx = 0;
        arr[idx] = vin;
        arr[++idx] = terminalCode;
        arr[++idx] = simCard;
        arr[++idx] = orgName;
        arr[++idx] = financeName;
        arr[++idx] = vehicleType;
        arr[++idx] = vehicleModel;
        arr[++idx] = "" + totalWorkTime;
        arr[++idx] = formatAcc(acc);
        arr[++idx] = formatGps(acc);
        arr[++idx] = dataUpdateTime;
        arr[++idx] = gpsTime;
        arr[++idx] = formatLock(super.getLockStatus());
        arr[++idx] = serviceStatus == null ? "" : ServiceStatus.fromValue(serviceStatus).getName();
        arr[++idx] = serviceStartDate;
        arr[++idx] = serviceEndDate;
        arr[++idx] = "" + servicePeriod;
        arr[++idx] = formatSaleStatus(saleStatus);
        arr[++idx] = contractNumber;
        arr[++idx] = customerName;
        arr[++idx] = contractNumber;
        arr[++idx] = alarmName;
        arr[++idx] = alarmNumber;
        arr[++idx] = formatDebugStatus(debugStatus);
        arr[++idx] = debugBeginTime;
        arr[++idx] = debugEndTime;
        arr[++idx] = registDate;
        return arr;
    }

    private String formatAcc(Integer acc) {
        if (null == acc || 0 == acc) {
            return "关";
        } else {
            return "开";
        }
    }

    private String formatGps(Integer gps) {
        if (null == gps || 0 == gps) {
            return "不定位";
        } else {
            return "已定位";
        }
    }

    private final StringBuilder builder = new StringBuilder();
    private final ArrayList<String> lockList = Lists.newArrayList();

    private String formatLock(Map<String, Integer> lockStatus) {
        lockList.clear();
        Optional.ofNullable(lockStatus)
                .ifPresent(e ->
                    e.forEach((k, v) -> {
                        String value = LockItem.getValue(k);
                        String s = builder.append(value).append("-").append(formatLockStr(v)).toString();
                        lockList.add(s);
                        builder.delete(0, s.length());}
                    )
                );

        return org.apache.commons.lang3.StringUtils.join(lockList, ",");
    }

    private String formatLockStr(Integer status) {
        if (1 == status) {
            return "已锁";
        } else {
            return "未锁";
        }
    }

    private String formatSaleStatus(Integer saleStatus) {
        if (null == saleStatus || 0 == saleStatus) {
            return "未售";
        } else {
            return "已售";
        }
    }

    private String formatDebugStatus(Integer debugStatus) {
        if (null == debugStatus || 0 == debugStatus) {
            return "不可调试";
        } else {
            return "可调式";
        }
    }
}
