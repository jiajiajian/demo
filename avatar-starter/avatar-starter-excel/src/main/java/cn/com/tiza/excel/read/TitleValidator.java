package cn.com.tiza.excel.read;

import lombok.extern.slf4j.Slf4j;

/**
 * Excel 标题校验器
 */
@Slf4j
public class TitleValidator {

    protected int row;

    protected ExcelReader reader;

    private Object titles;

    /**
     * @param titles 表头字符串数组
     */
    public TitleValidator(Object titles) {
        this(1, titles);
    }

    /**
     * @param row    开始行 以1开始
     * @param titles
     */
    public TitleValidator(int row, Object titles) {
        this.row = row - 1;
        this.titles = titles;
    }

    public void setReader(ExcelReader reader) {
        this.reader = reader;
    }

    /**
     * 默认校验逻辑
     *
     * @param sheet 表格页
     */
    public boolean verify(int sheet) {
        String[] titleValue = reader.getRowValues(sheet, row);
        String[] titlesArr = (String[]) titles;
        if (titleValue.length != titlesArr.length) {
            log.info("title length : {}, real length : {} ", titlesArr.length, titleValue.length);
            reader.addCellError(sheet, row, 0, "表头不正确");
            return false;
        }
        boolean result = true;
        for (int i = 0; i < titlesArr.length; i++) {
            if (!titlesArr[i].equalsIgnoreCase(titleValue[i])) {
                reader.addCellError(sheet, row, i, "表头不正确，预期值：" + titlesArr[i]);
                result = false;
            }
        }
        return result;
    }
}
