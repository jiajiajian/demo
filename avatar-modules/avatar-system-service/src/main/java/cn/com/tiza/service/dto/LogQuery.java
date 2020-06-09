package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogQuery extends Query {
    private String operateAccount;
    private Long startDate;
    private Long endDate;
    @Override
    protected void convertParams() {
        add("operateAccount", this.operateAccount, true);
        add("startDate", this.startDate);
        add("endDate", this.endDate);
    }

}
