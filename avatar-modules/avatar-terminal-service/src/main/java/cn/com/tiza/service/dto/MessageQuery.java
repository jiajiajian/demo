package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Data;

@Data
public class MessageQuery extends Query {

    private String keyword;

    private Long start;

    private Long end;

    private Integer cmd;

    @Override
    protected void convertParams() {

    }
}
