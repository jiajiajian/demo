package cn.com.tiza.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author tz0920
 */
@Data
public class MonthAvgWorkTimeQuery {
    private Integer tonnage;
    @NotNull
    private Integer beginMon;
    @NotNull
    private Integer endMon;
    private Long organizationId;
    @NotNull
    private List<Long> orgList;
    @NotNull
    private Integer mergeFlag;
}
