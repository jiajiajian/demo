package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import cn.com.tiza.domain.MaintenanceBind;
import lombok.Data;


/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class MaintenanceTacticsDto   {

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
	private String remark ;
	/**
	策略名称
	*/
	private String tacticsName ;
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
	 * 绑定的车辆类型和车辆型号列表
	 */
	private List<MaintenanceBind> bindList;

	public MaintenanceTacticsDto() {
	}


}
