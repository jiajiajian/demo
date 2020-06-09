package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
* 
* gen by beetlsql 2020-04-20
* @author tiza
*/
@Data
public class ChargeDetailDto   {

	private Long id ;
	/**
	起始月
	*/
	@NotNull
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

	public ChargeDetailDto() {
	}


}
