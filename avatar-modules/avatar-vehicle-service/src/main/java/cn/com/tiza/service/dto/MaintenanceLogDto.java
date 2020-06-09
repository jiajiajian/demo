package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;

import lombok.Data;


/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class MaintenanceLogDto   {

	/**
	主键ID
	*/
	private Long id ;
	/**
	处理状态
	*/
	private Integer handleStatus ;
	/**
	提醒小时数
	*/
	private Double remindHours ;
	/**
	处理情况
	*/
	private String handleResult ;
	/**
	处理时间
	*/
	private Long handleTime ;
	/**
	处理用户登录名
	*/
	private String handleUserAccount ;
	/**
	处理用户姓名
	*/
	private String handleUserRealname ;
	/**
	 * 保养指标
	 */
	private Integer maintenanceType;
	/**
	 * 间隔小时
	 */
	private Integer intervalHours;
	/**
	提醒时间
	*/
	private Long remindTime ;
	/**
	机器序列号
	*/
	private String vin ;

	public MaintenanceLogDto() {
	}


}
