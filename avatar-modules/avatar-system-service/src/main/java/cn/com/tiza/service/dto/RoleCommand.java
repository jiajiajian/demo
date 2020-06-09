package cn.com.tiza.service.dto;

import cn.com.tiza.dto.RoleType;
import cn.com.tiza.dto.UserType;
import lombok.Data;
import org.apache.commons.lang3.Validate;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Objects;

/**
 */
@Data
public class RoleCommand {
    /**
     *
     */
    @NotNull
    private RoleType roleType;
    /**
     * 融资机构ID
     */
    private Long financeId;

    private Long organizationId;
    /**
     * 角色名称
     */
    @NotNull
    private String roleName;

    /**
     * 角色描述
     */
    private String remark;

    public void validate() {
        if(Objects.nonNull(financeId)) {
            Validate.isTrue(Objects.isNull(organizationId));
        }
        if(Objects.nonNull(organizationId)) {
            Validate.isTrue(Objects.isNull(financeId));
        }
    }
}
