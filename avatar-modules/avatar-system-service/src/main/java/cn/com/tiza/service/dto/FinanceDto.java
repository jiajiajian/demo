package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Data
public class FinanceDto {
    /**
     * 机构名称
     */
    @NotNull(message = "融资机构名称不能为空")
    private String name;
    /**
     * 联系地址
     */
    private String contactAddress;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 电子邮箱
     */
    private String emailAddress;
    /**
     * 传真
     */
    private String faxNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 电话号码
     */
    private String telephoneNumber;

    private List<Long> orgIds;

    public FinanceDto() {
    }


}
