package cn.com.tiza.web.rest.vm;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class WorkConditionVM implements Serializable {
    /**
     * 工况时间
     */
    private Long time;
    /**
     * 接收时间
     */
    private Long rt;
    /**
     * 工况数据
     */
    private List<FunctionSetItemVM> itemVMList;
}
