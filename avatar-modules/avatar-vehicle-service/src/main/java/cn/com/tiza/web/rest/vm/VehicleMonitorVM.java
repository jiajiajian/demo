package cn.com.tiza.web.rest.vm;

import cn.com.tiza.service.dto.ServiceStatus;
import lombok.Data;

import java.util.Map;

/**
 * @author tiza
 */
@Data
public class VehicleMonitorVM {
    /**
     * 车辆ID
     */
    private Long id;
    /**
     * 机器序列号
     */
    private String vin;
    /**
     * 终端编号
     */
    private String terminalCode;
    /**
     * SIM卡号
     */
    private String cardCode;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 车辆类型
     */
    private String typeName;
    /**
     * 车辆型号
     */
    private String modelName;
    /**
     * ACC状态
     */
    private Integer acc;
    /**
     * 数据更新时间
     */
    private Long dataUpdateTime;

    /**
     * 数据更新时间
     */
    private String updateTime;
    /**
     * gps地址
     */
    private String gpsAddress;
    /**
     * 锁车状态
     */
    private Integer lock;
    /**
     * 销售状态
     */
    private Integer saleStatus;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户电话
     */
    private String phoneNumber;
    /**
     * 累计工作时间
     */
    private double totalWorkTime;
    /**
     * 定位状态
     */
    private Integer gps;
    /**
     * 终端状态
     */
    private Integer terminalStatus;

    private Integer oneLevelLock;
    private Integer twoLevelLock;
    private Integer threeLevelLock;
    private String gpsTime;
    private Double lon;
    private Double lat;
    private Double lastLon;
    private Double lastLat;
    /**
     * 锁车状态
     */
    private Map<String,Integer> lockStatus;

    /**
     * 信号强度
     */
    private String signalStrength;


    /**
     * 调试状态
     */
    private Integer testStatus;

    /**
     * 在线状态
     */
    private Integer online;

    public String[] toRow() {
        String[] arr = new String[28];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = terminalCode;
        arr[idx++] = cardCode;
        arr[idx++] = orgName;
        arr[idx++] = typeName;
        arr[idx++] = modelName;
        arr[idx++] = String.valueOf(totalWorkTime);
        arr[idx++] = formatAcc(acc);
        arr[idx++] = formatGps(gps);
        arr[idx++] = formatTerminalStatus(dataUpdateTime);
        arr[idx++] = updateTime;
        arr[idx++] = gpsAddress;
        arr[idx++] = formatLock(lock);
        arr[idx++] = formatSaleStatus(saleStatus);
        arr[idx++] = customerName;
        arr[idx++] = phoneNumber;
        return arr;
    }
    public String[] toRow2() {
        String[] arr = new String[28];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = terminalCode;
        arr[idx++] = cardCode;
        arr[idx++] = orgName;
        arr[idx++] = typeName;
        arr[idx++] = modelName;
        arr[idx++] = String.valueOf(totalWorkTime);
        arr[idx++] = formatAcc(acc);
        arr[idx++] = formatGps(gps);
        arr[idx++] = formatTerminalStatus(dataUpdateTime);
        arr[idx++] = updateTime;
        arr[idx++] = formatLock(lock);
        arr[idx++] = formatSaleStatus(saleStatus);
        arr[idx++] = customerName;
        arr[idx++] = phoneNumber;
        return arr;
    }
    public String[] toDashBoardRow() {
        String[] arr = new String[28];
        int idx = 0;
        arr[idx++] = vin;
        arr[idx++] = terminalCode;
        arr[idx++] = orgName;
        arr[idx++] = typeName;
        arr[idx++] = modelName;
        arr[idx++] = formatTerminalStatus(dataUpdateTime);
        arr[idx++] = updateTime;
        arr[idx++] = gpsAddress;
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

    private String formatLock(Integer lock) {
        if (null == lock || 0 == lock) {
            return "未锁";
        } else {
            return "已锁";
        }
    }

    private String formatTestStatus(Integer testStatus) {
        if (null == testStatus || 0 == testStatus) {
            return "不可调试";
        } else {
            return "可调试";
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
        if(null!=dataUpdateTime&&dataUpdateTime+300000>System.currentTimeMillis()){
            return "在线";
        }else {
            return "离线";
        }
    }
}
