package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * 
 * @author villas
 */
@Data
public class ExportTaskVM {

    private Long id;
    private Long beginTime;
    private Long endTime;
    private String filePath;
    private Long fwpTaskId;
    private String name;
    private Integer status;
    private Integer dataType;
    private Integer vehicleAmount;
}
