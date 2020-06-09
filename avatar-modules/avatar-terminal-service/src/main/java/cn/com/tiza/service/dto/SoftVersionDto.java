package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
* 
* gen by beetlsql 2020-03-09
* @author tiza
*/
@Data
public class SoftVersionDto   {

	private Long id ;
	/**
	编号
	*/
	@NotBlank
	private String code ;
	/**
	采集功能集ID
	*/
	@NotNull
	private Long collectFunctionId ;
	private Long createTime ;
	/**
	锁车功能集ID
	*/
	@NotNull
	private Long lockFunctionId ;
	/**
	名称
	*/
	@NotBlank
	private String name ;
	/**
	描述
	*/
	private String remark ;

	public SoftVersionDto() {
	}


}
