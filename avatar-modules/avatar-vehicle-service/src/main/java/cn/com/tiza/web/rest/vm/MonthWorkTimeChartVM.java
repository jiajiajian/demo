package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author tz0920
 */
@Data
public class MonthWorkTimeChartVM {
    private List<String> xData;
    private List<BigDecimal> yData;
}
