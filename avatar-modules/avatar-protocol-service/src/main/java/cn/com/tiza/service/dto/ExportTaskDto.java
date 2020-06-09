package cn.com.tiza.service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 
 * @author villas
 */
@Data
public class ExportTaskDto {

    private Long beginTime;

    private Long endTime;

    private String filePath;

    private Integer dataType;

    @NotNull
    private Long fwpTaskId;

    @NotBlank
    private String name;

    private Integer status;

    private Integer vehicleAmount;

    @NotEmpty
    private List<String> inputVinList;

}
