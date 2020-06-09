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
public class VehicleServiceDateDto extends ExcelDto {
    private String vin;
    private String period;
    private String description;

    public VehicleServiceDateDto(int sheet, Row row) {
        super(sheet, row.getRowNum());
        int idx = 0;
        Cell cell = row.getCell(idx++);
        this.vin = getValue(cell);

        cell = row.getCell(idx++);
        this.period = getValue(cell);
        cell = row.getCell(idx++);
        this.description = getValue(cell);
    }

    public static final int IDX_VIN = 0;
    public static final int IDX_PERIOD = 1;
    public static final int IDX_DESCRIPTION = 2;

    public static RowValidator ruleValidator() {
        return new RowValidator(VehicleUpdateDateExcelRule.instance.rules);
    }

    public static class VehicleUpdateDateExcelRule {
        private static VehicleUpdateDateExcelRule instance = new VehicleUpdateDateExcelRule();

        private List<CellRule> rules = new ArrayList<>();

        private VehicleUpdateDateExcelRule() {
            this.rules.add(new CellRule(IDX_VIN + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_PERIOD + 1, DataType.INTEGER, true));
            this.rules.add(new CellRule(IDX_DESCRIPTION + 1, DataType.STRING));
        }

        public List<CellRule> getRules() {
            return rules;
        }
    }
}
