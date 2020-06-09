package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-21
 *
 * @author tiza
 */
@Data
@Table(name = "r_lock_car_log")
public class RemoteLockCarLog implements Serializable {

    @AutoID
    @AssignID("simple")
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 执行指令---对应数据库字典的id
     */
    private Long dicItemId;
    private String itemName;
    /**
     * 执行状态：0：成功  1：失败  2：超时  3：执行中  4：离线指令已发送 5：控制器锁车指令已发送成功 6：控制器锁车指令发送失败
     */
    private Integer status;
    private String statusName;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 操作用户姓名
     */
    private String operateRealname;
    /**
     * 执行时间
     */
    private Long operateTime;
    private String operateTimeFormat;
    /**
     * 机器序列号
     */
    private String vin;

    /*******非数据库字段*******/
    /**
     * 终端编码
     */
    private String terminalCode;
    /**
     * SIM卡号
     */
    private String simCode;
    /**
     * 车辆类型
     */
    private String typeName;
    /**
     * 车辆型号
     */
    private String modelName;
    /**
     * 机构
     */
    private Long organizationId;
    private String orgName;

    /**
     * check_id
     */
    private String checkId;

    /**
     * 数据响应时间
     */
    private Long resTime;

    private String responseTime;

    public RemoteLockCarLog() {
    }

    public boolean isNeedGetCmdState() {
        return this.checkId != null &&
                (this.status == 3 || this.status == 4);
    }
}
