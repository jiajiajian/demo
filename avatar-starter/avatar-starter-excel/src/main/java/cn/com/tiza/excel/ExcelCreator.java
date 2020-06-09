package cn.com.tiza.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 简单的Excel导出工具类
 *
 * @author tiza
 */
@Slf4j
public class ExcelCreator {

    /**
     * 生成文档类型，默认为.xls 还可设为.xlsx
     */
    private ExcelType type = ExcelType.XLSX;

    /**
     * 标题行
     */
    private String[] title;

    private List<String[]> headers;

    private List<String[]> footers;

    /**
     * Bean转换器，把bean转换为字符串数组
     */
    private BeanParser parser;

    private CellStyle headStyle;

    private CellStyle dataStyle;

    private Workbook workbook;

    public static ExcelCreator newInstance() {
        return new ExcelCreator();
    }

    /**
     * 构建一个Excel导出器，并设置标题行和类型转换器
     *
     */
    private ExcelCreator() {
    }

    public ExcelCreator init() {
        if (this.type == ExcelType.XLS) {
            this.workbook = new HSSFWorkbook();
        } else if (this.type == ExcelType.XLSX) {
            this.workbook = new SXSSFWorkbook();
        }
        initStyles();
        return this;
    }

    public ExcelType getType() {
        return type;
    }

    public void setType(ExcelType type) {
        if (type != null) {
            this.type = type;
        }
    }

    /**
     *
     * @param sheetName sheet name
     * @param title  标题行
     * @param parser Bean转换器，把bean转换为字符串数组
     * @param data   data to add
     * @param <T> 要导出数据的类型
     * @return
     */
    public <T> ExcelCreator addSheet(String sheetName, String[] title, BeanParser<T> parser, List<T> data) {
        return addSheet(sheetName, null, title, parser, null, data);
    }

    public <T> ExcelCreator addSheet(String sheetName, List<String[]> headers, String[] title, BeanParser<T> parser, List<String[]> footers,
                                    List<T> data) {
        this.headers = headers;
        this.title = title;
        this.footers = footers;
        this.parser = parser;
        fill(sheetName, data);
        return this;
    }

    /**
     * 把数据转换未Excel文档，并写入指定输出流中
     *
     * @param outputStream 输入流
     */
    public void create(OutputStream outputStream) {
        try{
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 调用关闭IO
     *
     * @param data
     * @param zipOutputStream
     */
    public <T> void createForZIP(List<T> data, ZipOutputStream zipOutputStream, String fileName) {
//        try(Workbook workbook = buildWorkbook(data)) {
//            ZipEntry z = new ZipEntry(fileName + suffix());
//            zipOutputStream.putNextEntry(z);
//            workbook.write(zipOutputStream);
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
    }

    public <T> void fill(String sheetName, List<T> data) {
        Sheet sheet = workbook.createSheet(sheetName);
        int index = 0;
        if (headers != null && headers.size() > 0) {
            for(String[] head : headers) {
                addRow(sheet, head, index ++, dataStyle);
            }
            //增加空行
            index ++;
        }
        if (title != null) {
            addRow(sheet, title, index++, headStyle);
        }
        if (data != null && data.size() > 0) {
            for (T t : data) {
                addRow(sheet, parser.parser(t), index++, dataStyle);
            }
        }
        if (footers != null && footers.size() > 0) {
            //增加空行
            index ++;
            for(String[] foot : footers) {
                addRow(sheet, foot, index ++, dataStyle);
            }
        }
    }

    /**
     * 在Excel指定行写入一行数据
     *
     * @param sheet
     * @param cellData
     * @param index
     */
    private void addRow(Sheet sheet, String[] cellData, int index, CellStyle style) {
        Row row = sheet.createRow(index);
        for (int i = 0; i < cellData.length; i++) {
            if (cellData[i] != null) {
                Cell cell = row.createCell(i);
                cell.setCellValue(cellData[i]);
                cell.setCellStyle(style);
            }
        }
    }

    public String suffix() {
        return "." + type.name().toLowerCase();
    }

    private void initStyles() {
        //普通字体
        Font normalFont = workbook.createFont();
        normalFont.setFontHeightInPoints((short) 10);

        //白色加粗字体
        Font boldFont = workbook.createFont();
        boldFont.setFontHeightInPoints((short) 10);
        boldFont.setBold(true);
        boldFont.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());

        //标题格式
        headStyle = workbook.createCellStyle();
        headStyle.setFont(boldFont);
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());

        //字符格式
        dataStyle = workbook.createCellStyle();
        dataStyle.setFont(normalFont);
    }
}
