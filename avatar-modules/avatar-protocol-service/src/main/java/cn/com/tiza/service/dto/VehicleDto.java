package cn.com.tiza.service.dto;

import cn.com.tiza.excel.ExcelDto;
import cn.com.tiza.web.rest.errors.ProtocolErrorConstants;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static cn.com.tiza.excel.read.ExcelTool.getValue;

/**
 * @author villas 2019-02-21
 */
@Data
public class VehicleDto extends ExcelDto {
    public static final Integer INDEX_VIN = 0;
    public static final Integer INDEX_TID = 1;
    public static final Integer INDEX_PLATE = 2;
    /**
     * 所属机构
     */
    private Long orgId;
    /**
     * 车牌号
     */
    private String plateNumber;
    /**
     * 终端编码
     */
    private String tid;
    /**
     * VIN码
     */
    @NotBlank
    @Size(min = 17, max = 17, message = ProtocolErrorConstants.VIN_LENGTH_SEVENTEEN)
    private String vin;

    public VehicleDto() {
    }

    public VehicleDto(int sheet, Row row) {
        super(sheet, row.getRowNum());
        this.vin = getValue(row.getCell(0));
    }
}
