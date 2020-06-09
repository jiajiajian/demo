package cn.com.tiza.excel;

import cn.com.tiza.web.Servlets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.vip.vjtools.vjkit.time.DateFormatUtil.PATTERN_DEFAULT_ON_SECOND;
import static com.vip.vjtools.vjkit.time.DateFormatUtil.formatDate;

/**
 * @author villas
 * @since 2019/6/15 15:02
 */
@Slf4j
public class ExcelWriter {

    private static final String EXCEL_POSTFIX = ".xlsx";

    private static final String GET = "get";

    public static <T> void exportExcel(HttpServletRequest request, HttpServletResponse response, String fileName,
                                       String[] headers, String[] columns, List<T> content) throws IOException {
        exportExcel(request, response, fileName, headers, columns, content, Function.identity());
    }

    /**
     * 导出excel文件
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param fileName the name of excel which will be exported
     * @param headers  the header of excel
     * @param columns  the field of bean or map
     * @param content  the data will be exported
     * @param handler  handle every bean or map if handler is not empty
     * @param <T>      the specific class type of object in list
     */
    public static <T, R> void exportExcel(HttpServletRequest request, HttpServletResponse response,
                                          String fileName, String[] headers, String[] columns,
                                          List<T> content, Function<T, R> handler) throws IOException {

        if (ArrayUtils.isEmpty(headers) || ArrayUtils.isEmpty(columns) || headers.length != columns.length) {
            throw new NullPointerException("headers or columns which is in the method of exportExcel is null!");
        }
        Objects.requireNonNull(handler, "bean's handler is null");
        try (Workbook wb = new SXSSFWorkbook(); OutputStream out = response.getOutputStream()) {
            Sheet sheet = wb.createSheet(fileName);
            // 生成Excel文件得第一行头部信息
            writeHeader(sheet, headers);
            // 生成Excel文件内容部分.
            writeData(sheet, columns, content, handler);
            response.setContentType("application/vnd.ms-excel; charset=UTF-8");
            // 设置让浏览器弹出下载对话框的Header.
            Servlets.setFileDownloadHeader(request, response, fileName + EXCEL_POSTFIX);
            // 输出Excel文件.
            wb.write(out);
        }
    }

    /**
     * 导出过程中需要将毫秒格式的时间转换为 yyyy-MM-dd HH:mm:ss 格式的
     *
     * @param millisecond millisecond
     * @return format time of ""
     */
    public static String timeConvert(Long millisecond) {
        return Optional.ofNullable(millisecond).map(time -> formatDate(PATTERN_DEFAULT_ON_SECOND, time)).orElse("");
    }

    private static void writeHeader(Sheet sheet, String[] headers) {
        Row row = sheet.createRow(0);
        Cell cell;
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }

    /**
     * 生成excel内容区
     *
     * @param columns 一行中所有列的列名称
     * @param content 导出的数据
     */
    private static <T, R> void writeData(Sheet sheet, String[] columns, List<? extends T> content, Function<T, R> handler) {
        if (CollectionUtils.isEmpty(content)) {
            return;
        }
        Row row;
        Cell cell;
        R type = handler.apply(content.get(0));
        boolean isMap = type instanceof Map;
        //此处不能使用get(i)的形式遍历，否则当传入的是LinkList时，会非常慢
        int rowNum = 0;
        for (T t : content) {
            R r = handler.apply(t);
            row = sheet.createRow(++rowNum);
            for (int j = 0; j < columns.length; j++) {
                cell = row.createCell(j);
                Object res = getValue(r, columns[j], isMap);
                cell.setCellValue(Objects.isNull(res) ? "" : res.toString());
            }
        }
    }

    /**
     * 获取对象指定字段的值
     *
     * @param r         对象(普通的JavaBean或者Map)
     * @param fieldName 字段或key
     * @param isMap     是否Map
     * @param <R>       T
     * @return 结果
     */
    private static <R> Object getValue(R r, String fieldName, boolean isMap) {
        if (isMap) {
            return ((Map) r).get(fieldName);
        }
        Class<?> cls = r.getClass();
        String methodName = getMethodName(fieldName);
        try {
            return cls.getMethod(methodName).invoke(r);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("get value exception: {}", e.getMessage());
            return null;
        }
    }

    private static String getMethodName(String fieldName) {
        return GET + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    }

    private ExcelWriter() {
    }
}

