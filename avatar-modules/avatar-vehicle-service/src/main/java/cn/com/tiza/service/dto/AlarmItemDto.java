package cn.com.tiza.service.dto;

import java.math.*;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
* 
* gen by beetlsql 2020-03-10
* @author tiza
*/
@Data
public class AlarmItemDto   {
	/**
	机构ID
	*/
	@NotNull
	private Long organizationId ;
	private ArrayList<String> items;

	public AlarmItemDto() {
	}


}
