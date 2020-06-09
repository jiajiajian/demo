package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;

import lombok.Data;


/**
* 
* gen by beetlsql 2020-04-01
* @author tiza
*/
@Data
public class MaintenanceBindDto   {

	/**
	主键ID
	*/
	private Long id ;
	/**
	创建时间
	*/
	private Long createTime ;
	/**
	创建用户登录名
	*/
	private String createUserAccount ;
	/**
	创建用户姓名
	*/
	private String createUserRealname ;
	/**
	保养策略ID
	*/
	private Long maintenanceTacticsId ;
	/**
	车辆型号
	*/
	private Long vehicleModelId ;
	/**
	车辆类型
	*/
	private Long vehicleTypeId ;

	public MaintenanceBindDto() {
	}


}
