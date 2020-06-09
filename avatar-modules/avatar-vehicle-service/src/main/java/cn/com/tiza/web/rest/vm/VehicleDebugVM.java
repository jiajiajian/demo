package cn.com.tiza.web.rest.vm;

import cn.com.tiza.domain.VehicleDebugLog;
import cn.com.tiza.service.dto.LockItem;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class VehicleDebugVM extends LockVM{
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 机器序列号
     */
    private String vin;

    /**
     * 调试结果
     */
    private Integer status;

    /**
     * 机构
     */
    private Long organizationId;
    private String orgName;

    /**
     * 调试人
     */
    private String loginName;
    /**
     * 终端编码
     */
    private String terminalCode;
    private Long terminalId;
    /**
     * SIM卡编码
     */
    private String cardCode;
    /**
     * ACC状态
     */
    private Integer acc;
    /**
     * GPS状态
     */
    private Integer gps;
    /**
     * 锁定状态
     */
    private Integer lock;
    /**
     * GPS地址
     */
    private String gpsAddress;
    /**
     * 车辆类型
     */
    private String typeName;
    /**
     * 车辆型号
     */
    private String modelName;
    private Long vehicleModelId;

    private Integer online;
    /**
     * 更新时间
     */
    private String updateTime;
    /**
     * 各状态数量
     */
    private Integer count;
    /**
     * 调试项集合
     */
    private List<VehicleDebugLog> cmdList;

    private Double lon;

    private Double lat;


    private Integer oneLevelLock;
    private Integer twoLevelLock;
    private Integer threeLevelLock;
    private Long dataUpdateTime;
    private String debugBeginTime;
    private String debugEndTime;
    private Integer testStatus;
    /**
     * 锁车状态
     */
    private Map<String, Integer> lockStatus;
    /**
     * 终端状态
     */
    private Integer terminalStatus;

    public String[] toRow() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = terminalCode;
        arr[idx++] = cardCode;
        arr[idx++] = formatStatus(status);
        arr[idx++] = orgName;
        arr[idx++] = typeName;
        arr[idx++] = modelName;
        arr[idx++] = formatAcc(acc);
        arr[idx++] = formatGps(gps);
        arr[idx++] = formatTerminalStatus(dataUpdateTime);
        arr[idx++] = formatLock(super.getLockStatus());
        arr[idx++] = updateTime;
        arr[idx++] = gpsAddress;
        arr[idx++] = loginName;
        arr[idx++] = formatTestStatus(testStatus);
        return arr;
    }

    public String[] toRow2() {
        String[] arr = new String[16];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = terminalCode;
        arr[idx++] = cardCode;
        arr[idx++] = formatStatus(status);
        arr[idx++] = orgName;
        arr[idx++] = typeName;
        arr[idx++] = modelName;
        arr[idx++] = formatAcc(acc);
        arr[idx++] = formatGps(gps);
        arr[idx++] = formatTerminalStatus(dataUpdateTime);
        arr[idx++] = formatLock(super.getLockStatus());
        arr[idx++] = updateTime;
        arr[idx++] = loginName;
        arr[idx++] = formatTestStatus(testStatus);
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
        lockStatus.forEach((k, v) -> {
            String value = LockItem.getValue(k);
            String s = builder.append(value).append("-").append(formatLockStr(v)).toString();
            lockList.add(s);
            builder.delete(0, s.length());
        });

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

    private String formatTerminalStatus(Long dataUpdateTime) {
        if (null != dataUpdateTime && dataUpdateTime + 300000 > System.currentTimeMillis()) {
            return "在线";
        } else {
            return "离线";
        }
    }

    private String formatStatus(Integer status) {
        if (null == status || 0 == status) {
            return "待调试";
        } else if(1==status){
            return "调试成功";
        }else {
            return "调试失败";
        }
    }

    private String formatTestStatus(Integer testStatus) {
        if (null == testStatus || 0 == testStatus) {
            return "不可调试";
        }else {
            return "可调试";
        }
    }
}
