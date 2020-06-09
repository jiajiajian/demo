package cn.com.tiza.web.rest.vm;


import cn.com.tiza.domain.MaintenanceBind;
import cn.com.tiza.domain.MaintenanceInfo;
import lombok.Data;

import java.util.List;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class MaintenanceTacticsVM   {

	/**
	主键ID
	*/
	private Long id ;
	/**
	关联车型
	*/
	private String bindType ;
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
	 * 关联车型列表
	 */
	private List<MaintenanceBind> bindList;
	/**
	 * 保养信息列表
	 */
	private List<MaintenanceInfoVM> infoList;

	private String orgName;

	private String bindNames;

	public MaintenanceTacticsVM() {
	}


}
