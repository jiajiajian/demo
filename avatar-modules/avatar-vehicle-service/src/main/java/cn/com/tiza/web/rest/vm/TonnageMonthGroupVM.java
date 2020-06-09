package cn.com.tiza.web.rest.vm;

import lombok.Data;

@Data
public class TonnageMonthGroupVM {

    private String month;

    private Integer tonnage;

    private String type;

    private Integer num;
}
