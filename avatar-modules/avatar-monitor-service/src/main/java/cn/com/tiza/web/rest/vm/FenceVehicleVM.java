package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
* 
* gen by beetlsql 2020-03-31
* @author tiza
*/
@Data
public class FenceVehicleVM   {

	private Long id ;
	/**
	围栏id
	*/
	private Long fenceId ;
	private String vin ;

	public FenceVehicleVM() {
	}


}
