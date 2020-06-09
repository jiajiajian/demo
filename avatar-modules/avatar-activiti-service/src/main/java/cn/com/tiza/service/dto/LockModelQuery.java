package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Data;

@Data
public class LockModelQuery extends Query {

    private String category;

    private String name;

    @Override
    protected void convertParams() {

    }
}
