package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
* 
* gen by beetlsql 2020-05-12
* @author tiza
*/
@Data
public class TlaDto   {

	private Long createTime ;
	private String createUserAccount ;
	@NotNull
	private Long organizationId ;
	@NotNull
	private String tla ;
	@NotNull
	private String tlaId ;

	public TlaDto() {
	}


}
