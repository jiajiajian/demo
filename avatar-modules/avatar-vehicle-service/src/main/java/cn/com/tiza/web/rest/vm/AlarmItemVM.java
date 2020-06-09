package cn.com.tiza.web.rest.vm;


import lombok.Data;

import java.util.List;

/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class AlarmItemVM {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 报警项
     */
    private String alarmItem;
    private List<String> alarmItemList;
    /**
     * 机构ID
     */
    private Long organizationId;
    private String orgName;
    /**
     更新时间
     */
    private String updateTime ;
    /**
     更新用户登录名
     */
    private String updateUserAccount ;
    public AlarmItemVM() {
    }


}
