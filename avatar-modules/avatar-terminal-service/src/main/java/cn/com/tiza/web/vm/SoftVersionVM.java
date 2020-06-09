package cn.com.tiza.web.vm;


import lombok.Data;

/**
* 
* gen by beetlsql 2020-03-09
* @author tiza
*/
@Data
public class SoftVersionVM   {

	private Long id ;
	/**
	编号
	*/
	private String code ;
	/**
	采集功能集ID
	*/
	private Long collectFunctionId ;
	private Long createTime ;
	/**
	锁车功能集ID
	*/
	private Long lockFunctionId ;
	/**
	名称
	*/
	private String name ;
	/**
	描述
	*/
	private String remark ;

	private String collectName;

	private String lockName;

	public SoftVersionVM() {
	}


}
