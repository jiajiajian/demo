package cn.com.tiza.excel;

/**
 * Excel转换的对象
 *
 * @author tiza
 */
public abstract class ExcelDto {

    private int sheet;
    private int row;

    public ExcelDto() {
    }

    public ExcelDto(int sheet, int row) {
        this.sheet = sheet;
        this.row = row;
    }

    public int getSheet() {
        return sheet;
    }

    public void setSheet(int sheet) {
        this.sheet = sheet;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

}
