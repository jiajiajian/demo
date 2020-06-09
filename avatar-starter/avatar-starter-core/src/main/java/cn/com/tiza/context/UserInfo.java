package cn.com.tiza.context;

import cn.com.tiza.domain.BaseUser;
import cn.com.tiza.dto.UserType;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author tiza
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo implements BaseUser, Serializable {

    private Long id;
    private UserType userType;
    /**
     * 用户所在融资机构
     */
    private Long financeId;
    /**
     * 用户所在机构
     */
    private Long orgId;
    /**
     * 用户所在根机构
     */
    private Long rootOrgId;
    private String loginName;
    private String realName;
    private String ipAddress;
    private String userAgent;

    public String toJson() {
        return JsonMapper.defaultMapper().toJson(this);
    }
}
