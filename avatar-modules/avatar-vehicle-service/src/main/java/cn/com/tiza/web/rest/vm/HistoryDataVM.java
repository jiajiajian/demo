package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.util.List;

@Data
public class HistoryDataVM {
    /**
     * 数据列表
     */
    private List<WorkConditionVM> conditionVMList;
    /**
     * 数据总条数
     */
    private Integer pageCount;
}
