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
@Table(name="v_fault_dict")
public class FaultDict  implements Serializable,Entity {

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
     * 故障字典文件
     */
    private String fileName ;
    /**
     * 故障字典名称
     */
    private String name ;
    /**
     * 机构ID
     */
    private Long organizationId ;
    /**
     * 备注
     */
    private String remark ;

    private Long updateTime ;

    private String updateUserAccount ;

    private String updateUserRealname ;
	
    public FaultDict() {
    }

}
