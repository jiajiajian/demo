package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;

import lombok.Data;


/**
* 
* gen by beetlsql 2020-03-17
* @author tiza
*/
@Data
public class VehicleDebugDto   {

	/**
	主键ID
	*/
	private Long id ;
	/**
	调试结果
	*/
	private Integer status ;
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
	调试开始时间
	*/
	private Long debugBeginTime ;
	/**
	调试结束时间
	*/
	private Long debugEndTime ;
	/**
	调试人
	*/
	private Long debugUserId ;
	/**
	更新时间
	*/
	private Long updateTime ;
	/**
	更新用户登录名
	*/
	private String updateUserAccount ;
	/**
	更新用户姓名
	*/
	private String updateUserRealname ;
	/**
	机器序列号
	*/
	private String vin ;

	/**
	 * 机构ID
	 */
	private Long organizationId;

	public VehicleDebugDto() {
	}


}
