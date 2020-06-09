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
* gen by beetlsql 2020-04-20
* @author tiza
*/
@Data
@Table(name="v_charge")
public class Charge  implements Serializable {

	@AutoID
	@AssignID("simple")
    private Long id ;
    /**
     * 配置状态 0:未配置 1:已配置
     */
    private Integer configFlag ;
    /**
     * 组织id
     */
    private Long organizationId ;
    /**
     * 终端型号
     */
    private String terminalModel ;
	
    public Charge() {
    }

}
