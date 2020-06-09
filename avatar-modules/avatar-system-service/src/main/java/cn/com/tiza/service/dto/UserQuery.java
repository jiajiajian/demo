package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 0837
 */
@Getter
@Setter
public class UserQuery extends Query {

    private String userType;
    private String name;
    private Long roleId;

    @Override
    protected void convertParams() {
        add("name", this.name, true);
        add("roleId", this.roleId);
        if(this.userType != null) {
            switch (this.userType){
                case "ADMIN":
                    add("userType",1);
                    break;
                case "ORGANIZATION":
                    add("userType",2);
                    break;
                case "FINANCE":
                    add("userType",3);
                    break;
                default:
                    add("userType",this.userType);
            }
        }
    }
}
