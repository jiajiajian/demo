package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-05-11
 *
 * @author tiza
 */
@Data
@Table(name = "tls_report_vehicle_daily")
public class ReportVehicleDaily implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 年月日，格式：20191129
     */
    private Integer dateVal;
    /**
     * 年月，格式：201911
     */
    private Integer monthVal;
    /**
     * 当日是否上线
     */
    private Integer totalOnline;
    /**
     * 年，格式：2019
     */
    private Integer yearVal;
    private Long createTime;
    /**
     * 挖掘行驶时长分布
     */
    private String digTravelRatio;
    /**
     * 最后当前平均油耗
     */
    private Double endDfConsumption;
    /**
     * 最后燃油油位
     */
    private Double endFuelLevel;
    /**
     * 最后平均油耗
     */
    private Double endLfConsumption;
    /**
     * 发动机未运转时长（毫秒）
     */
    private Long engineNotRunTime;
    /**
     * 发动机运转时长（毫秒）
     */
    private Long engineRunTime;
    /**
     * 发动机转速时长分布
     */
    private String engineSpeedRatio;
    /**
     * 发动机状态时长分布（0：未运转，1：运转）
     */
    private String engineStateRatio;
    /**
     * 风扇状态时长分布
     */
    private String fanStateRatio;
    /**
     * 锤击时长（毫秒）
     */
    private Long hammerTime;
    /**
     * 最大冷却液温度
     */
    private Double maxCoolantTemp;
    /**
     * 最大进气管温度
     */
    private Double maxEimTemp;
    /**
     * 最大液压油温度
     */
    private Double maxHOilTemp;
    /**
     * 各功率时长分布
     */
    private String pmRatio;
    /**
     * 当日工作时间（毫秒）
     */
    private Long totalWorkingTime;
    private String vin;
    /**
     * 工作模式时长分布
     */
    private String workModeRatio;

    public ReportVehicleDaily() {
    }

}
