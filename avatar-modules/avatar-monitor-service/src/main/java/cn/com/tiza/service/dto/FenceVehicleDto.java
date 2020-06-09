package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;

import lombok.Data;


/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class FenceVehicleDto   {

	private Long id ;
	/**
	围栏id
	*/
	private Long fenceId ;
	private String vin ;

	public FenceVehicleDto() {
	}


}
