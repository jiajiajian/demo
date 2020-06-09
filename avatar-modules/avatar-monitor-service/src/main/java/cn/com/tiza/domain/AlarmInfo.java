package cn.com.tiza.domain;

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
@Table(name="v_alarm_info")
public class AlarmInfo  implements Serializable {

	@AssignID("simple")
    private Long id ;
    private String vin ;
    /**
     * 类型 1:报警 2:故障 3:围栏报警
     */
    private AlarmType alarmType ;
    /**
     *  1:普通机构 2:融资机构
     */
    private Integer orgType ;
    /**
     * 组织id
     */
    private Long organizationId ;
    /**
     * 地址
     */
    private String address ;
    /**
     * 报警项(针对报警)
     */
    private String alarmCode ;
    private String area ;
    /**
     * 开始时间
     */
    private Long beginTime ;
    private String city ;
    /**
     * 围栏id(针对围栏报警)
     */
    private Long fenceId ;

    private String lat ;
    private String lon ;

    private String province ;
    /**
     * SPN.FMI
     */
    private String spnFmi ;
    private String tla ;
    private int frequency;

	
    public AlarmInfo() {
    }

}
