package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.util.Map;

import static cn.com.tiza.service.LockService.isOnline;

/**
 * gen by beetlsql 2020-03-31
 *
 * @author tiza
 */
@Data
@Table(name = "r_lock")
public class Lock implements Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 远程锁车id
     */
    private Long id;
    /**
     * VIN码
     */
    private String vin;
    /**
     * 指令表Id
     */
    private Long commandId;

    private int flag = 0;

    private Long dicItemId;

    /*-----------------非数据库字段---------------------*/
    /**
     * 终端编码
     */
    private String terminalCode;
    /**
     * SIM卡号
     */
    private String simCode;
    /**
     * 锁车状态
     */
    private Integer lock;
    private String lockName;
    private Integer oneLevelLock;
    private Integer twoLevelLock;
    private Integer threeLevelLock;
    /**
     * ACC状态
     */
    private Integer acc;
    private String accName;
    /**
     * 定位状态
     */
    private Integer gps;
    private String gpsName;
    /**
     * 数据响应时间
     */
    private Long dataUpdateTime;
    private String responseTime;
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
     * 销售状态
     */
    private Integer saleStatus;
    private String saleStatusName;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户电话
     */
    private String phoneNumber;
    /**
     * 终端状态
     */
    private String terminalStatus;

    private Integer state;

    private String ipAddress;

    private String operateUsername;

    private String itemCode;

    private String itemName;

    private String runStateName;

    private String executeTime;

    public Lock() {
    }

    public Lock(String vin) {
        this.vin = vin;
    }

    private Map<String, Integer> lockStatus;

    public String getAccName() {
        return this.accName(this.acc);
    }

    public String getGpsName() {
        return this.gpsName(this.gps);
    }

    public String getLockName() {
        return this.lockName(this.lock);
    }

    public String getSaleStatusName() {
        return this.saleStatusName(this.saleStatus);
    }

    public String getTerminalStatus() {
        return this.terminalStatus(this.dataUpdateTime);
    }

    private String accName(Integer acc) {
        if (null == acc || 0 == acc) {
            return "ACC关";
        } else {
            return "ACC开";
        }
    }

    private String gpsName(Integer gps) {
        if (null == gps || 0 == gps) {
            return "不定位";
        } else {
            return "已定位";
        }
    }

    private String lockName(Integer lock) {
        if (null == lock || 0 == lock) {
            return "未锁车";
        } else {
            return "已锁车";
        }
    }

    private String saleStatusName(Integer saleStatus) {
        if (null == saleStatus || 0 == saleStatus) {
            return "未售";
        } else {
            return "已售";
        }
    }

    private String terminalStatus(Long dataUpdateTime) {
        return isOnline(dataUpdateTime) ? "在线" : "离线";
    }

    public String getRunStateName() {
        return this.state == null ? "" : CmdRes.getStatus(this.state);
    }

    /*----------not in db------------*/

}
