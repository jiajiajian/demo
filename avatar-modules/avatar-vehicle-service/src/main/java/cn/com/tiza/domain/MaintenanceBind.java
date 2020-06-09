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
* gen by beetlsql 2020-04-01
* @author tiza
*/
@Data
@Table(name="v_maintenance_bind")
public class MaintenanceBind  implements Serializable {

	@AutoID
	@AssignID("simple")
    /**
     * 主键ID
     */
    private Long id ;
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
     * 保养策略ID
     */
    private Long maintenanceTacticsId ;
    /**
     * 车辆型号
     */
    private Long vehicleModelId ;
    private String modelName;
    /**
     * 车辆类型
     */
    private Long vehicleTypeId ;
    private String typeName;

	
    public MaintenanceBind() {
    }

}
