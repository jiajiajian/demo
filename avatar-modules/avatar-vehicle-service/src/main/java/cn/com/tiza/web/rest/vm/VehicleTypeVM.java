package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
* 
* gen by beetlsql 2020-03-10
* @author tiza
*/
@Data
public class VehicleTypeVM   {

	/**
	主键ID
	*/
	private Long id ;

	/**
	类型名称
	*/
	private String name ;
	/**
	机构
	*/
	private String orgName ;
	private Long organizationId ;
	/**
	更新时间
	*/
	private String updateTime ;
	/**
	更新用户登录名
	*/
	private String updateUserAccount ;

	public VehicleTypeVM() {
	}

	public String[] toRow() {
		String[] arr = new String[4];
		int idx = 0;
		arr[idx++] = name;
		arr[idx++] = orgName;
		arr[idx++] = updateUserAccount;
		arr[idx++] = updateTime;
		return arr;
	}

}
