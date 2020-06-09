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
@Table(name="v_cmd_debug")
public class CmdDebug  implements Serializable {

	@AutoID
	@AssignID("simple")
    /**
     * 主键ID
     */
    private Long id ;
    /**
     * 配置指令
     */
    private String cmd ;
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
    /**
     * 机构ID
     */
    private Long organizationId ;
    /**
     * 备注
     */
    private String remark ;
    /**
     * 车型ID
     */
    private Long vehicleTypeId ;
	
    public CmdDebug() {
    }

}
