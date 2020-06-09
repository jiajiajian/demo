package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author tz0920
 */
@Data
public class MonthAvgWorkTimeVM {
    private String month;
    private Long workTime;
    private BigDecimal workHour;
}
