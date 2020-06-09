package cn.com.tiza.web.rest.vm;


import com.vip.vjtools.vjkit.time.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.List;

/**
 *
 * @author tiza
 */
@Data
public class FinanceVM {

    /**
     * 主键ID
     */
    private Long id;

    private String name;
    /**
     * 机构名称
     */
    private String orgName;
    /**
     * 联系地址
     */
    private String contactAddress;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 创建时间
     */
    private Long createTime;
    /**
     * 创建用户登录名
     */
    private String createUserAccount;
    /**
     * 创建用户姓名
     */
    private String createUserRealname;
    /**
     * 电子邮箱
     */
    private String emailAddress;
    /**
     * 传真
     */
    private String faxNo;
    /**
     * 官方网站
     */
    private String officialSite;
    /**
     * 备注
     */
    private String remark;
    /**
     * 电话号码
     */
    private String telephoneNumber;

    private List<Long> orgIds;

    public FinanceVM() {
    }

    public String[] toRow() {
        //"融资机构名称","关联机构","机构地址","联系人","联系电话","电子邮箱","创建人","创建时间"
        String[] row = new String[10];
        int idx = 0;
        row[idx ++] = name;
        row[idx ++] = orgName;
        row[idx ++] = contactAddress;
        row[idx ++] = contactName;
        row[idx ++] = telephoneNumber;
        row[idx ++] = emailAddress;
        row[idx ++] = createUserAccount;
        row[idx ++] = FastDateFormat.getInstance("yyyy-MM-dd").format(createTime) ;
        return row;
    }
}
