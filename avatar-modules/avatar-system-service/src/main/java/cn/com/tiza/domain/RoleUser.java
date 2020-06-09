package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;

/**
 * 角色用户关联
 */
@Data
@Table(name = "base_role_user")
public class RoleUser {


    private Long roleId;
    private Long userId;
    private Long createTime;

    public RoleUser() {

    }

    public RoleUser(Long roleId, Long userId) {
        this.roleId = roleId;
        this.userId = userId;
        this.createTime=System.currentTimeMillis();
    }
}
