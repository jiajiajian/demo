package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;


/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class MaintenanceInfoDto   {

	/**
	主键ID
	*/
	private Long id ;
	/**
	小时数
	*/
	private Integer hours ;
	/**
	保养指标
	*/
	private Integer maintenanceType ;
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
	保养条目
	*/
	private String maintenanceContent ;
	/**
	备注
	*/
	private String remark ;
	/**
	策略ID
	*/
	private Long tacricsId ;
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
	 * 保养条目列表
	 */
	private List<String> contents;

	public MaintenanceInfoDto() {
	}


}
