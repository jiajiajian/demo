package cn.com.tiza.domain;
import java.math.*;
import java.util.Date;
import java.sql.Timestamp;
import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;
import java.io.Serializable;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.AssignID;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
@Table(name="v_maintenance_log")
public class MaintenanceLog  implements Serializable {

	@AutoID
	@AssignID("simple")
    /**
     * 主键ID
     */
    private Long id ;
    /**
     * 保养指标
     */
    private Integer maintenanceType;
    /**
     * 间隔小时
     */
    private Integer intervalHours;
    /**
     * 处理状态
     */
    private Integer handleStatus ;
    /**
     * 提醒小时数
     */
    private Double remindHours ;
    /**
     * 处理情况
     */
    private String handleResult ;
    /**
     * 处理时间
     */
    private Long handleTime ;
    /**
     * 处理用户登录名
     */
    private String handleUserAccount ;
    /**
     * 处理用户姓名
     */
    private String handleUserRealname ;
    /**
     * 提醒时间
     */
    private Long remindTime ;
    /**
     * 机器序列号
     */
    private String vin ;

    /**
     * 保养内容
     */
    private String maintenanceContent;
	
    public MaintenanceLog() {
    }

}
