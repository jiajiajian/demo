package cn.com.tiza.web.rest.vm;


import lombok.Data;

/**
* 
* gen by beetlsql 2020-05-12
* @author tiza
*/
@Data
public class TlaVM   {

	private Long id ;
	private Long createTime ;
	private String createUserAccount ;
	private Long organizationId ;
	private String tla ;
	private String tlaId ;
	private String orgName ;

	public TlaVM() {
	}


}
