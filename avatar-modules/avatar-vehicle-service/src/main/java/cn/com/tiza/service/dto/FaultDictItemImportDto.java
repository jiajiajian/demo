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
public class FaultDictItemImportDto extends ExcelDto {
    private String fmi;
    private String fmiName;
    private String rootOrgName;
    private String spn;
    private String spnName;
    private String tla;

    public FaultDictItemImportDto(int sheet, Row row) {
        super(sheet, row.getRowNum());
        int idx = 0;
        Cell cell = row.getCell(idx++);
        this.tla = getValue(cell);

        cell = row.getCell(idx++);
        this.spn = getValue(cell);

        cell = row.getCell(idx++);
        this.fmi = getValue(cell);

        cell = row.getCell(idx++);
        this.spnName = getValue(cell);

        cell = row.getCell(idx++);
        this.fmiName = getValue(cell);
    }

    public static final int IDX_TLA = 0;
    public static final int IDX_SPN = 1;
    public static final int IDX_FMI = 2;
    public static final int IDX_SPN_NAME = 3;
    public static final int IDX_FMI_NAME = 4;

    public static RowValidator ruleValidator() {
        return new RowValidator(FaultDictItemImportDto.FaultDictItemExcelRule.instance.rules);
    }

    public static class FaultDictItemExcelRule {
        private static FaultDictItemImportDto.FaultDictItemExcelRule instance = new FaultDictItemExcelRule();

        private List<CellRule> rules = new ArrayList<>();

        private FaultDictItemExcelRule() {
           // this.rules.add(new CellRule(IDX_TLA + 1, DataType.STRING));
            this.rules.add(new CellRule(IDX_SPN + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_FMI + 1, DataType.STRING, true));
            this.rules.add(new CellRule(IDX_SPN_NAME + 1, DataType.STRING, true));
           // this.rules.add(new CellRule(IDX_FMI_NAME + 1, DataType.STRING));
        }

        public List<CellRule> getRules() {
            return rules;
        }
    }

}
