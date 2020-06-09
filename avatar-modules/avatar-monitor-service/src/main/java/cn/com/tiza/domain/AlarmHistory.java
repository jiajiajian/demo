package cn.com.tiza.domain;
import java.math.*;

import cn.com.tiza.dto.AlarmType;
import org.beetl.sql.core.annotatoin.Table;

import lombok.Data;
import java.io.Serializable;

import org.beetl.sql.core.annotatoin.AssignID;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
@Table(name="v_alarm_history")
public class AlarmHistory  implements Serializable {

	@AssignID("simple")
    private Long id ;
    /**
     * 报警状态(0:未解除 1:已解除)
     */
    private Integer alarmState ;
    /**
     * 报警类型 1:报警 2:故障 3:围栏报警
     */
    private AlarmType alarmType ;
    /**
     *  1:普通机构 2:融资机构
     */
    private Integer orgType ;
    /**
     * 发生地
     */
    private String address ;
    /**
     * 报警项
     */
    private String alarmCode ;
    private String area ;
    /**
     * 开始时间
     */
    private Long beginTime ;
    private String city ;
    /**
     * 持续时长(小时)
     */
    private BigDecimal duration ;
    /**
     * 解除时间
     */
    private Long endTime ;
    /**
     * 故障参数
     */
    private String faultParameter ;
    /**
     * 围栏id(针对围栏报警)
     */
    private Long fenceId ;
    private String lat ;
    private String lon ;
    private Long organizationId ;
    private String province ;
    /**
     * SPN.FMI
     */
    private String spnFmi ;
    private String tla ;
    private int frequency;
    private String vin ;
    private String recentlyCondition ;

    public AlarmHistory() {
    }

}
