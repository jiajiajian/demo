package cn.com.tiza.web.rest.vm;


import cn.com.tiza.dto.SelectOption;
import lombok.Data;

import java.util.List;

/**
* 
* gen by beetlsql 2020-03-23
* @author tiza
*/
@Data
public class NoticeStrategyVM   {
	private Long id ;
	private String code ;
	private String name ;
	private List<SelectOption> userList;
	private List<SelectOption> roleList;
	private List<NoticeMethodOption> remindWay;
	private String remindWayStr;
	public NoticeStrategyVM() {
	}


}
