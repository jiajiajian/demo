package cn.com.tiza.service.dto;

import java.math.*;
import java.util.Date;
import java.sql.Timestamp;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
* 
* gen by beetlsql 2020-03-10
* @author tiza
*/
@Data
public class VehicleModelDto   {

	/**
	其他
	*/
	private String description ;
	/**
	类型名称
	*/
	@NotNull
	private String name ;
	/**
	机构ID
	*/
	@NotNull
	private Long organizationId ;
	/**
	类型ID
	*/
	@NotNull
	private Long vehicleTypeId ;

	/**
	 * 吨位
	 */
	@NotNull
	private Integer tonnage;

	public VehicleModelDto() {
	}


}
