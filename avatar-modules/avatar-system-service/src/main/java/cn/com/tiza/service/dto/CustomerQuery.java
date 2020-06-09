package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
 * @author tiza
 */
@Getter
@Setter
public class CustomerQuery extends Query {

    private String q;

    @Override
    protected void convertParams() {
        add("q", this.q, true);
    }

}
