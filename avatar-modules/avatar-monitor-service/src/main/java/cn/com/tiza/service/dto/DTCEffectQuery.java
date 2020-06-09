package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Data;

@Data
public class DTCEffectQuery extends Query {

    private Long startTime;

    private Long endTime;

    @Override
    protected void convertParams() {
        super.add("startTime", this.startTime);
        super.add("endTime", this.endTime);
    }
}
