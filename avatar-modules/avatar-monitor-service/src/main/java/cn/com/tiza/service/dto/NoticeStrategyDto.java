package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * gen by beetlsql 2020-04-01
 *
 * @author tiza
 */
@Data
public class NoticeStrategyDto {

    private Long id;
    /**
     * 1:普通机构 2:融资机构
     */
    private Integer orgType;
    private String description;
    private Long organizationId;
    @NotNull
    private String code;
    private String name;
    /**
     * 通知方式 SMS,EMAIL,NON
     */
    @NotNull
    private List<String> remindWay;
    private List<Long> roleIds;
    private List<Long> userIds;

    public NoticeStrategyDto() {
    }


}
