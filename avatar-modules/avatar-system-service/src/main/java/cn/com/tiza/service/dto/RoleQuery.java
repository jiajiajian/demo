package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import cn.com.tiza.dto.RoleType;
import lombok.Getter;
import lombok.Setter;

/**
 * 角色基本信息
 * @author tiza
 */
@Setter
@Getter
public class RoleQuery extends Query{

    private RoleType roleType;
    private String roleName;

    @Override
    protected void convertParams() {
        add("roleName",this.roleName,true);
        if(roleType != null) {
            add("roleType", this.roleType.getValue());
        }
    }
}
