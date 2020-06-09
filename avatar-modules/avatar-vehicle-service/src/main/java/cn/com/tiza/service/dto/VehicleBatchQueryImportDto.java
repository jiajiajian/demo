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
public class VehicleBatchQueryImportDto extends ExcelDto {
    private String vin;

    public VehicleBatchQueryImportDto(int sheet, Row row) {
        super(sheet, row.getRowNum());
        int idx = 0;
        Cell cell = row.getCell(idx++);
        this.vin = getValue(cell);
    }

    public static final int IDX_VIN = 0;

    public static RowValidator ruleValidator() {
        return new RowValidator(VehicleUpdateDateExcelRule.instance.rules);
    }

    public static class VehicleUpdateDateExcelRule {
        private static VehicleUpdateDateExcelRule instance = new VehicleUpdateDateExcelRule();

        private List<CellRule> rules = new ArrayList<>();

        private VehicleUpdateDateExcelRule() {
            this.rules.add(new CellRule(IDX_VIN + 1, DataType.STRING, true));
        }

        public List<CellRule> getRules() {
            return rules;
        }
    }
}
