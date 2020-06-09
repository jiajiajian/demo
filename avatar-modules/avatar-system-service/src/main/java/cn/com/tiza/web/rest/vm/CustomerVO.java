package cn.com.tiza.web.rest.vm;


import lombok.Data;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Data
public class CustomerVO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 手机号码
     */
    private String phoneNumber;
    /**
     * 报警联系人
     */
    private String alarmName;
    /**
     * 24H报警电话
     */
    private String alarmNumber;
    /**
     * 登录名
     */
    private String name;
    /**
     * 组织ID
     */
    private Long organizationId;

    private String orgName;
    /**
     * 机手名
     */
    private String ownerName;
    /**
     * 机手电话
     */
    private String ownerNumber;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 创建用户登录名
     */
    private String createUserAccount;

    public CustomerVO() {
    }


    public String[] toRow() {
        //"客户名称", "客户电话", "所属机构", "机手", "机手电话", "报警联系人", "24H报警电话", "创建人", "创建时间"
        String[] row = new String[10];
        int idx = 0;
        row[idx ++] = name;
        row[idx ++] = phoneNumber;
        row[idx ++] = orgName;
        row[idx ++] = ownerName;
        row[idx ++] = ownerNumber;
        row[idx ++] = alarmName;
        row[idx ++] = alarmNumber;
        row[idx ++] = createUserAccount;
        row[idx ++] = FastDateFormat.getInstance("yyyy-MM-dd").format(createTime) ;
        return row;
    }
}
