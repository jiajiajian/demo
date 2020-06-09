package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Data;

@Data
public class MyTaskQuery extends Query {

    private String userId;

    @Override
    protected void convertParams() {

    }
}
