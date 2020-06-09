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
@Table(name="v_notice_strategy")
public class NoticeStrategy  implements Serializable {

	@AutoID
	@AssignID("simple")
    private Long id ;
    /**
     * 1:普通机构 2:融资机构
     */
    private Integer orgType ;
    private String code;
    private String name;
    private String description ;
    private Long organizationId ;
    /**
     * 通知方式 SMS,EMAIL,NON
     */
    private String remindWay ;
    private String roleIds ;
    private String userIds ;
    private Long updateTime ;
    private String updateUserAccount ;
    private String updateUserRealname ;

    public NoticeStrategy() {
    }

}
