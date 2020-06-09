package cn.com.tiza.excel.read;


import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;

import java.math.BigDecimal;

/**
 * @author tiza
 */
public class ExcelTool {

    private static DataFormatter dataFormatter = new DataFormatter();

    public static String getStringValue(Cell cell) {
        String value = cell.getRichStringCellValue().getString();
        return value == null ? null : value.trim();
    }

    public static String[] getStringValues(Row row) {
        int lastCellNum = row.getLastCellNum();
        String[] values = new String[lastCellNum];
        for (int i = 0; i < lastCellNum; i++) {
            values[i] = getStringValue(row.getCell(i));
        }
        return values;
    }

    public static String getValue(Cell cell) {
        String value = null;

        if (cell != null) {
            switch (cell.getCellType()) {
                case _NONE:
                    break;
                case NUMERIC:
                    value = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                    value = trimZero(value);
                    break;
                case STRING:
                    value = cell.getStringCellValue();
                    break;
                case FORMULA:
                    switch(cell.getCachedFormulaResultType()) {
                        case STRING:
                            RichTextString str = cell.getRichStringCellValue();
                            if(str != null && str.length() > 0) {
                                value = str.getString();
                            }
                            break;
                        case NUMERIC:
                            value = BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString();
                            break;
                        case BOOLEAN:
                            value = String.valueOf(cell.getBooleanCellValue());
                            break;
                        case ERROR:
                            value = ErrorEval.getText(cell.getErrorCellValue());
                            break;
                        default:
                            throw new IllegalStateException("Unexpected cell cached formula result type: " + cell.getCachedFormulaResultType());
                    }
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case ERROR:
                    break;
            }
            if (value != null) {
                value = value.trim();
                if (value.isEmpty()) {
                    value = null;
                }
            }
        }
        return value;
    }

    private static String trimZero(String str) {
        if (str != null && str.indexOf(".") != -1 && (str.endsWith("0") || str.endsWith("."))) {
            str = str.substring(0, str.length() - 1);
            str = trimZero(str);
        }
        return str;
    }

    public static void appendComment(CreationHelper factory, Drawing drawing, Row row, Integer index, String message) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            cell = row.createCell(index);
        }
        appendComment(factory, drawing, cell, message);
    }

    public static void appendComment(CreationHelper factory, Drawing drawing, Cell cell, String message) {
        //Validate.notNull(cell, "单元格不能为null");
        if (cell.getCellComment() == null) {
            setComment(factory, drawing, cell, message);
        } else {
            Comment comment = cell.getCellComment();
            RichTextString richTextString = comment.getString();
            RichTextString newTextString = factory.createRichTextString(richTextString.getString() + "\n" + message);
            comment.setString(newTextString);
            cell.setCellComment(comment);
        }
    }

    public static void setComment(CreationHelper factory, Drawing drawing, Cell cell, String message) {
        //Validate.notNull(cell, "单元格不能为null");
        ClientAnchor anchor = factory.createClientAnchor();
        if (factory instanceof HSSFCreationHelper) {
            anchor.setCol1(4);
            anchor.setRow1(2);
            anchor.setCol2(7);
            anchor.setRow2(5);
        }
        Comment comment = drawing.createCellComment(anchor);
        RichTextString msg = factory.createRichTextString(message);
        comment.setString(msg);
        cell.setCellComment(comment);
    }


}
