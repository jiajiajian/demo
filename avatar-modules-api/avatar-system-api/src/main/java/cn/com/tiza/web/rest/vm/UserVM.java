package cn.com.tiza.web.rest.vm;

import cn.com.tiza.dto.UserType;
import com.vip.vjtools.vjkit.time.DateFormatUtil;
import lombok.Data;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.List;
import java.util.Set;

@Data
public class UserVM {
    /**
     * ID
     */
    private Long id;

    private UserType userType;

    private Long financeId;

    private String financeName;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 真实姓名
     */
    private String realName;

    private Long rootOrgId;
    /**
     * 组织ID
     */
    private Long organizationId;

    private String organizationName;

    private List<Long> roleIds;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 电子邮件
     */
    private String emailAddress;
    /**
     * 角色
     */
    private String roleName;

    /**
     * 过期时间
     */
    private Long expirationDate;
    /**
     * 登录次数
     */
    private Integer loginTimes;

    /**
     * 最后登录IP
     */
    private String latestLoginIp;
    /**
     * 最后登录时间
     */
    private Long latestLoginTime;
    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 用户所有的子系统，菜单，权限信息
     */
    private Set<Long> authorizes;

    public String[] toRow() {
//    "登录名","姓名","机构","用户类型","角色","联系电话","登录次数","最后登录时间","最后登录IP","注册时间","过期时间"
        String[] row = new String[12];
        int idx = 0;
        row[idx ++] = loginName;
        row[idx ++] = realName;
        row[idx ++] = organizationName == null? organizationName:financeName;
        row[idx ++] = userType.getName();
        row[idx ++] = roleName;
        row[idx ++] = phoneNumber;
        row[idx ++] = loginTimes == null? "0" : loginTimes.toString();
        if(latestLoginTime == null) {
            row[idx ++] = "";
        } else {
            row[idx ++] = DateFormatUtil.formatDate("yyyy-MM-dd HH:mm:ss",latestLoginTime);
        }
        row[idx ++] = latestLoginIp == null ?"": latestLoginIp;
        row[idx ++] = createTime == null? "": DateFormatUtil.formatDate("yyyy-MM-dd",createTime);
        if(expirationDate == null) {
            row[idx ++] = "永久有效";
        } else {
            row[idx ++] = DateFormatUtil.formatDate("yyyy-MM-dd",expirationDate);
        }
        return row;
    }
}
