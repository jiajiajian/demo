package cn.com.tiza.web.rest.vm;


import lombok.Data;

import java.math.BigDecimal;

/**
* 
* gen by beetlsql 2020-04-20
* @author tiza
*/
@Data
public class ChargeDetailVM   {

	private Long id ;
	/**
	起始月
	*/
	private Integer begin ;
	/**
	结束月
	*/
	private Integer end ;
	/**
	费用配置id
	*/
	private Long chargeId ;
	/**
	月缴费用
	*/
	private BigDecimal fee ;

	public ChargeDetailVM() {
	}


}
