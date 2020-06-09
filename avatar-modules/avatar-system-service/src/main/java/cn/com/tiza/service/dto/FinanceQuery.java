package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tiza
 */
@Getter
@Setter
public class FinanceQuery extends Query {

    private String name;

    @Override
    protected void convertParams() {
        add("name", this.name, true);
    }

}
