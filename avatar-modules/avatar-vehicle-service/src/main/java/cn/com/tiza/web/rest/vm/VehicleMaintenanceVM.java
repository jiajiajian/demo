package cn.com.tiza.web.rest.vm;


import lombok.Data;

import java.util.List;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class VehicleMaintenanceVM   {

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
	private String orgName;
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
	 * 保养内容集合
	 */
	private List<String> content;

	private String createTimeStr;

	public VehicleMaintenanceVM() {
	}

	public String[] toRow() {
		String[] arr = new String[28];
		int idx = 0;
		arr[idx++] = itemName;
		arr[idx++] = itemDetail;
		arr[idx++] = orgName;
		arr[idx++] = createUserAccount;
		arr[idx++] = createTimeStr;
		return arr;
	}

}
