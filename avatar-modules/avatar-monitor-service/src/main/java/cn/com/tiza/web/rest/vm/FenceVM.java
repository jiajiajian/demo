package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class FenceVM   {

	/**
	主键
	*/
	private Long id ;
	/**
	0:出围栏报警 1:进围栏报警
	*/
	private Integer alarmType ;
	/**
	类型 1：图形围栏 2：行政围栏 3:时间围栏
	*/
	private Integer fenceType ;
	/**
	关联车辆数
	*/
	private Integer vehicleNum ;
	/**
	区域信息
	*/
	private String area ;
	/**
	名称
	*/
	private String name ;
	/**
	组织
	*/
	private Long organizationId ;
	/**
	1:普通机构 2:融资机构
	*/
	private Integer orgType ;
	private String orgName ;
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

	public FenceVM() {
	}


}
