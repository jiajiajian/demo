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
* gen by beetlsql 2020-04-14
* @author tiza
*/
@Data
@Table(name="v_vehicle_realtime")
public class VehicleRealtime  implements Serializable {

	@AutoID
	@AssignID("simple")
    /**
     * 主键ID
     */
    private Long id ;
    /**
     * ACC状态（0：ACC关，1：ACC开）
     */
    private Integer acc ;
    /**
     * 故障状态（0：无故障 1：有故障）
     */
    private Integer faultStatus ;
    /**
     * 定位状态（0：GPS不定位，1：GPS已定位）
     */
    private Integer gps ;
    /**
     * 控制器锁车状态（0：未锁车 1：仪表锁车）
     */
    private Integer lock ;
    /**
     * 继电器一级锁车（0：未锁车 1：一级锁车）
     */
    private Integer oneLevelLock ;
    /**
     * 继电器三级锁车（0：未锁车 1：三级锁车）
     */
    private Integer threeLevelLock ;
    /**
     * 继电器二级锁车（0：未锁车 1：二级锁车）
     */
    private Integer twoLevelLock ;
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
     * 最新数据纬度
     */
    private BigDecimal lastLat ;
    /**
     * 最新数据经度
     */
    private BigDecimal lastLon ;
    /**
     * gps纬度
     */
    private BigDecimal lat ;
    /**
     * gps经度
     */
    private BigDecimal lon ;
    /**
     * 累计工作时间
     */
    private Long totalWorkTime ;
    /**
     * 机器序列号
     */
    private String vin ;
    private String signalStrength ;

    public VehicleRealtime() {
    }

}
