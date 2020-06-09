package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class AlarmInfoVM   {

	private Long id ;
	/**
	类型 1:报警 2:故障 3:围栏报警
	*/
	private Integer alarmType ;
	/**
	 1:普通机构 2:融资机构
	*/
	private Integer orgType ;
	/**
	地址
	*/
	private String address ;
	/**
	报警项(针对报警)
	*/
	private String alarmCode ;
	private String area ;
	/**
	开始时间
	*/
	private Long beginTime ;
	private String city ;
	/**
	故障参数
	*/
	private String faultParameter ;
	/**
	围栏id(针对围栏报警)
	*/
	private Long fenceId ;
	/**
	FMI
	*/
	private String fmi ;
	private String lat ;
	private String lon ;
	/**
	组织id
	*/
	private Long organizationId ;
	private String province ;
	/**
	SPN
	*/
	private String spn ;
	private String vin ;

	private String spnFmi;

	/**
	 * 故障码
	 */
	private String faultCode;
	private String spnName;
	private String fmiName;

	private String tla;

	public AlarmInfoVM() {
	}


}
