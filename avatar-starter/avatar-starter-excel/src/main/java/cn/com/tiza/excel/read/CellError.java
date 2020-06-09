package cn.com.tiza.excel.read;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * 单元格错误
 * @author tiza
 */
public class CellError {

    private Row row;
    private int col;
    private String msg;

    public CellError(Row row, int col, String msg) {
        this.row = row;
        this.col = col;
        this.msg = msg;
    }

    public Cell getCell() {
        Cell cell = row.getCell(col);
        if(cell == null) {
            return row.createCell(col);
        }
        return cell;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CellError{" +
                "row=" + row.getRowNum() +
                ", col=" + col +
                ", msg='" + msg + '\'' +
                '}';
    }
}
