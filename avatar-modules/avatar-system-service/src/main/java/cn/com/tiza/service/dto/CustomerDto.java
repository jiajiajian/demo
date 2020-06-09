package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * gen by beetlsql 2020-03-06
 *
 * @author tiza
 */
@Data
public class CustomerDto {

    /**
     * 登录名
     */
    @NotBlank
    private String name;
	/**
	 * 手机号码
	 */
    @NotBlank
	private String phoneNumber;
	/**
	 * 组织ID
	 */
    @NotNull
	private Long organizationId;
    /**
     * 报警联系人
     */
    private String alarmName;
    /**
     * 24H报警电话
     */
    private String alarmNumber;
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

    public CustomerDto() {
    }


}
