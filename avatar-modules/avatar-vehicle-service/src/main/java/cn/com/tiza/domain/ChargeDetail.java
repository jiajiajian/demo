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
@Table(name="v_charge_detail")
public class ChargeDetail  implements Serializable {

	@AutoID
	@AssignID("simple")
    private Long id ;
    /**
     * 起始月
     */
    private Integer begin ;
    /**
     * 结束月
     */
    private Integer end ;
    /**
     * 费用配置id
     */
    private Long chargeId ;
    /**
     * 月缴费用
     */
    private BigDecimal fee ;
	
    public ChargeDetail() {
    }

}
