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
public class VehicleDebugLogDto   {

	/**
	主键ID
	*/
	private Long id ;
	/**
	调试结果
	*/
	private Integer status ;
	/**
	反馈内容
	*/
	private String content ;
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
	调试时间
	*/
	private Long debugTime ;
	/**
	调试人
	*/
	private Long debugUserId ;
	/**
	调试项KEY
	*/
	private String itemKey ;
	/**
	调试项
	*/
	private String itemName ;
	/**
	机器序列号
	*/
	private String vin ;

	public VehicleDebugLogDto() {
	}


}
