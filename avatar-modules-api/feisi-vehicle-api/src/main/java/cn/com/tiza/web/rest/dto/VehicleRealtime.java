package cn.com.tiza.web.rest.dto;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* 
* gen by beetlsql 2020-03-10
* @author tiza
*/
@Data
@Table(name="v_vehicle_realtime")
public class VehicleRealtime implements Serializable {

	@AutoID
	@AssignID("simple")
    /**
     * 主键ID
     */
    private Long id ;
    /**
     * 锁车状态
     */
    private Integer lock ;
    /**
     * 累计工作时间
     */
    private Integer totalWorkTime ;
    /**
     * ACC状态
     */
    private Long acc ;
    /**
     * 数据更新时间
     */
    private Long dataUpdateTime ;
    /**
     * 调试结束时间
     */
    private Long debugEndTime ;
    /**
     * 调试开始时间
     */
    private Long debugStartTime ;
    /**
     * 备注
     */
    private String description ;
    /**
     * 定位状态
     */
    private Long gps ;
    /**
     * GPS位置
     */
    private String gpsAddress ;
    /**
     * 当前区县
     */
    private String gpsArea ;
    /**
     * 当前市
     */
    private String gpsCity ;
    /**
     * 当前省
     */
    private String gpsProvince ;
    /**
     * 定位有效时间
     */
    private Long gpsTime ;
    /**
     * 当前纬度
     */
    private BigDecimal lat ;
    /**
     * 当前经度
     */
    private BigDecimal lon ;
    /**
     * 机器序列号
     */
    private String vin ;
	
    public VehicleRealtime() {
    }

}
