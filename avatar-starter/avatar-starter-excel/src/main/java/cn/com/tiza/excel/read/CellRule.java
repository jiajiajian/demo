package cn.com.tiza.excel.read;

import cn.com.tiza.excel.DataType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import java.math.BigDecimal;
import java.util.function.Function;

import static cn.com.tiza.util.ValidateTool.*;

/**
 * 单元格验证规则
 * @author tiza
 */
public class CellRule {

    private int idx;

    private DataType type;

    private boolean required;

    private Function function;

    /**
     *
     * @param idx 开始列，以1开始
     * @param type 数据类型
     */
    public CellRule(int idx, DataType type) {
        this(idx, type, false);
    }

    /**
     *
     * @param idx 开始列，以1开始
     * @param type 数据类型
     * @param required
     */
    public CellRule(int idx, DataType type, boolean required) {
        this.idx = idx - 1;
        this.type = type;
        this.required = required;
    }

    /**
     *
     * @param idx 开始列，以1开始
     * @param type 数据类型
     * @param required
     * @param function 自定义验证函数，用于验证待选项等规则
     */
    public CellRule(int idx, DataType type, boolean required, Function function) {
        this.idx = idx - 1;
        this.type = type;
        this.required = required;
        this.function = function;
    }

    /**
     * 验证单元格数据格式是否正确
     * @param row
     * @return
     */
    public CellError validate(Row row) {
        Cell cell = row.getCell(idx);
        String value = ExcelTool.getValue(cell);
        String msg = null;
        //单元格为空列
        if(valueIsEmpty(value)) {
            //非空检查
            return required ? new CellError(row, idx, "不能为空") : null;
        }

        switch (type) {
            case ENUM:
                msg = (String) function.apply(value);
                break;
            case NUMBER:
                if(! isNumber(value)) {
                    msg = "必须是数字";
                }
                break;
            case DATE:
                msg = validateDate(cell);
                break;
            case POSITIVE_NUMBER:
                if(! isPositiveNumber(value)) {
                    msg = "必须是非负整数";
                }
                break;
            case NEGATIVE_NUMBER:
                if(! isNegatineNumber(value)) {
                    msg = "必须是非正整数";
                }
                break;
            case INTEGER:
                msg = validateInteger(value);
                break;
            case Z_INTEGER:
                msg = validateZIndex(value);
                break;
            case FLOAT:
                if(! isFloat(value)) {
                    msg = "必须是小数";
                }
                break;
            case POSITIVE_FLOAT:
                if(! isPosttiveFloat(value)) {
                    msg = "必须是正小数";
                }
                break;
            case NEGATIVE_FLOAT:
                if(! isNegativeFloat(value)) {
                    msg = "必须是负小数";
                }
                break;
            case UN_POSITIVE_FLOAT:
                if(! isUnpositiveFloat(value)) {
                    msg = "必须是非正小数";
                }
                break;
            case UN_NEGATIVE_FLOAT:
                if(! isUnNegativeFloat(value)) {
                    msg = "必须是非负小数";
                }
                break;
            case BIG_DECIMAL:
                try {
                    new BigDecimal(value);
                } catch (Exception e) {
                    msg = "必须是数值";
                }
                break;
            default:
                break;
        }
        return msg == null ? null : new CellError(row, idx, msg);
    }

    private String validateDate(Cell cell) {
        try {
            DateUtil.getJavaDate(cell.getNumericCellValue());
        } catch (Exception e) {
            return "必须是日期";
        }
        return null;
    }

    private String validateInteger(String value) {
        if(! isInteger(value)) {
            boolean flag = false;
            if (isNumber(value)) {
                BigDecimal b1 = new BigDecimal(value);
                BigDecimal b2 = new BigDecimal(b1.intValue());
                flag = b1.compareTo(b2) == 0;
            }
            if (!flag) {
                return  "必须是整数";
            }
        }
        return null;
    }

    private String validateZIndex(String value) {
        if(! isZIndex(value)) {
            boolean flag = false;
            if (isNumber(value)) {
                BigDecimal b1 = new BigDecimal(value);
                BigDecimal b2 = new BigDecimal(b1.intValue());
                flag = b1.compareTo(b2) == 0;
            }
            if (!flag) {
                return "必须是正整数";
            }
        }
        return null;
    }

    /**
     * 判断是否为空
     * @param value
     * @return
     */
    public static boolean valueIsEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }

}
