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
* gen by beetlsql 2020-03-10
* @author tiza
*/
@Data
@Table(name="v_vehicle_type")
public class VehicleType  implements Serializable,Entity {

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
     * 其他
     */
    private String description ;
    /**
     * 类型名称
     */
    private String name ;
    /**
     * 机构ID
     */
    private Long organizationId ;
    /**
     * 更新时间
     */
    private Long updateTime ;
    /**
     * 更新用户登录名
     */
    private String updateUserAccount ;
    /**
     * 更新用户姓名
     */
    private String updateUserRealname ;
	
    public VehicleType() {
    }

}
