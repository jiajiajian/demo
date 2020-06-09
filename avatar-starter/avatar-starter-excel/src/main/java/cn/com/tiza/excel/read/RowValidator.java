package cn.com.tiza.excel.read;

import org.apache.poi.ss.usermodel.Row;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tiza
 */
public class RowValidator {

    private List<CellRule> rules;

    public RowValidator(List<CellRule> rules) {
        this.rules = rules;
    }

    /**
     * 验证行数据格式正确
     * @param row
     * @return 错误的单元格
     */
    List<CellError> validate(Row row) {
        return rules.stream()
                .map(rule -> rule.validate(row))
                .filter(error -> error != null)
                .collect(Collectors.toList());
    }
}
