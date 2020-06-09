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
@Table(name="v_fence")
public class Fence  implements Serializable,Entity {

	@AutoID
	@AssignID("simple")
    /**
     * 主键
     */
    private Long id ;
    /**
     * 0:出围栏报警 1:进围栏报警
     */
    private Integer alarmType ;
    /**
     * 类型 1：图形围栏 2：行政围栏 3:时间围栏
     */
    private Integer fenceType ;
    /**
     * 关联车辆数
     */
    private Integer vehicleNum ;
    /**
     * 区域信息
     */
    private String area ;
    /**
     * 创建信息
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
     * 名称
     */
    private String name ;
    /**
     * 组织
     */
    private Long organizationId ;
    /**
     * 1:普通机构 2:融资机构
     */
    private Integer orgType ;
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
	
    public Fence() {
    }

}
