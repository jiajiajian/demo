package cn.com.tiza.service.dto;

import cn.com.tiza.excel.DataType;
import cn.com.tiza.excel.ExcelDto;
import cn.com.tiza.excel.read.CellRule;
import cn.com.tiza.excel.read.RowValidator;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

import static cn.com.tiza.excel.read.ExcelTool.getValue;


/**
 * gen by beetlsql 2020-03-10
 *
 * @author tiza
 */
@Data
public class VehicleImportDto extends ExcelDto {
    private String vin;
    private String terminalCode;
    private String simCard;
    private String organization;
    private String period;
    private Long organizationId;
    private String vehicleModel;
    private Long vehicleModelId;
    private String vehicleType;
    private Long vehicleTypeId;

    public VehicleImportDto(int sheet, Row row) {
        super(sheet, row.getRowNum());
        int idx = 0;
        Cell cell = row.getCell(idx++);
        this.vin = getValue(cell);

        cell = row.getCell(idx++);
        this.terminalCode = getValue(cell);

        cell = row.getCell(idx++);
        this.simCard = getValue(cell);

        cell = row.getCell(idx++);
        this.organization = getValue(cell);

        cell = row.getCell(idx++);
        this.period = getValue(cell);

        cell = row.getCell(idx++);
        this.vehicleType = getValue(cell);

        cell = row.getCell(idx++);
        this.vehicleModel = getValue(cell);
    }

    public static final int IDX_VIN = 0;
    public static final int IDX_TERMINAL_CODE = 1;
    public static final int IDX_SIM_CARD = 2;
    public static final int IDX_ORGANIZATION = 3;
    public static final int IDX_PERIOD = 4;
    public static final int IDX_VEHICLE_TYPE = 5;
    public static final int IDX_VEHICLE_MODEL = 6;

    public static RowValidator ruleValidator() {
        return new RowValidator(VehicleExcelRule.instance.rules);
    }

    public static class VehicleExcelRule {
        private static VehicleExcelRule instance = new VehicleExcelRule();

        private List<CellRule> rules = new ArrayList<>();

        private VehicleExcelRule() {
            this.rules.add(new CellRule(IDX_VIN + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_TERMINAL_CODE + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_SIM_CARD + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_ORGANIZATION + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_PERIOD + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_VEHICLE_TYPE + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_VEHICLE_MODEL + 1, DataType.STRING, true));
        }

        public List<CellRule> getRules() {
            return rules;
        }
    }
}
