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
public class VehicleMaintenanceDto   {

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
	备注
	*/
	private String description ;
	/**
	保养项内容
	*/
	private String itemDetail ;
	/**
	保养项名称
	*/
	private String itemName ;
	/**
	所属组织
	*/
	private Long organizationId ;
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
	 * 保养内容列表
	 */
	private List<String> content;

	public VehicleMaintenanceDto() {
	}


}
