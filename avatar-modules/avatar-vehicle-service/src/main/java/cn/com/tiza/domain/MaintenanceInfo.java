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
@Table(name="v_maintenance_info")
public class MaintenanceInfo  implements Serializable {

	@AutoID
	@AssignID("simple")
    /**
     * 主键ID
     */
    private Long id ;
    /**
     * 小时数
     */
    private Integer hours ;
    /**
     * 保养指标
     */
    private Integer maintenanceType ;
    /**
     * 创建时间
     */
    private Long createTime ;
    /**
     * 创建用户登录名
     */
    private String createUserAccount ;
    /**
     * 创建用户姓名
     */
    private String createUserRealname ;
    /**
     * 保养条目
     */
    private String maintenanceContent ;
    /**
     * 备注
     */
    private String remark ;
    /**
     * 策略ID
     */
    private Long tacticsId ;
	
    public MaintenanceInfo() {
    }

}
