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
* gen by beetlsql 2020-05-07
* @author tiza
*/
@Data
@Table(name="tls_report_vehicle_monthly")
public class ReportVehicleMonthly  implements Serializable {

	@AutoID
	@AssignID("simple")
    private Long id ;
    /**
     * 年月，格式：201911
     */
    private Integer monthVal ;
    /**
     * 吨位
     */
    private Integer tonnage ;
    /**
     * 当月是否上线
     */
    private Integer totalOnline ;
    /**
     * 年，格式：2019
     */
    private Integer yearVal ;
    private Long createTime ;
    /**
     * 当月工作时间（毫秒）
     */
    private Long totalWorkingTime ;
    private String vin ;
	
    public ReportVehicleMonthly() {
    }

}
