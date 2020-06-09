package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author villas
 */
@Getter
@Setter
public class TaskQuery extends Query {

    private String name;

    private Integer activeStatus;

    @Override
    protected void convertParams() {
        add("name", this.name, true);
        add("activeStatus", this.activeStatus, false);
    }

}
