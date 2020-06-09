package cn.com.tiza.excel.read;

import cn.com.tiza.excel.ExcelDto;
import cn.com.tiza.excel.ExcelType;
import cn.com.tiza.excel.POIUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @param <T>
 * @author tiza
 */
@Slf4j
public class ExcelReader<T> implements Closeable {

    private int startRow;

    private BeanBuilder<T> builder;

    private TitleValidator titleValidator;

    private RowValidator rowValidator;

    private List<CellError> cellErrors = new ArrayList<>();

    private Workbook wb;
    private CreationHelper factory;
    protected Drawing drawing;

    public static <T> ExcelReader<T> createInstance(int startRow, BeanBuilder<T> builder) {
        return new ExcelReader<>(startRow, builder, null, null);
    }

    /**
     * @param startRow       开始行，以1开始
     * @param builder
     * @param titleValidator
     * @param rowValidator
     * @param <T>
     * @return
     */
    public static <T> ExcelReader<T> createInstance(int startRow, BeanBuilder<T> builder,
                                                    TitleValidator titleValidator, RowValidator rowValidator) {
        return new ExcelReader<>(startRow, builder, titleValidator, rowValidator);
    }

    /**
     *  create instance
     * @param builder built the bean of dto
     * @param titles the titles of excel table
     * @param checker null checker
     * @param <T> the type of dto
     * @return ExcelReader
     */
    public static <T> ExcelReader<T> createInstance(BeanBuilder<T> builder, Object titles,
                                                    Consumer<List<CellRule>> checker) {
        Objects.requireNonNull(checker);
        List<CellRule> rule = new ArrayList<>();
        checker.accept(rule);

        TitleValidator validator = Objects.isNull(titles) ? null : new TitleValidator(titles);
        return createInstance(2, builder, validator, new RowValidator(rule));
    }

    private ExcelReader(int startRow, BeanBuilder<T> builder, TitleValidator titleValidator, RowValidator rowValidator) {
        this.startRow = startRow;
        this.builder = builder;
        this.titleValidator = titleValidator;
        this.rowValidator = rowValidator;

        if (this.titleValidator != null) {
            this.titleValidator.setReader(this);
        }
    }

    public ExcelReader<T> create(InputStream inputStream) throws IOException {
        wb = WorkbookFactory.create(inputStream);
        factory = wb.getCreationHelper();
        drawing = wb.getSheetAt(0).createDrawingPatriarch();
        return this;
    }

    public List<T> resolve() {
        List<T> list = new ArrayList<>();
        int sheetIdx = 0;

        Sheet sheet = wb.getSheetAt(sheetIdx);

        //如果表头部正确，不再检查后续的数据项
        if (!titleVerify(sheetIdx)) {
            return list;
        }
        for (int i = startRow - 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null && rowVerify(row)) {
                list.add(builder.build(sheetIdx, row));
            }
        }
        return list;
    }

    public void writeErrorFile(String filePath) {
        writeErrorToExcel();
        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public byte[] writeErrorFileToBytes() throws IOException {
        writeErrorToExcel();
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            wb.write(bos);
            return bos.toByteArray();
        }
    }

    private void writeErrorToExcel() {
        this.cellErrors.forEach(error ->
                POIUtil.appendComment(factory, drawing, error.getCell(), error.getMsg()));
    }

    private boolean rowVerify(Row row) {
        if (rowValidator != null) {
            List<CellError> rowErrors = rowValidator.validate(row);
            if (rowErrors != null && rowErrors.size() > 0) {
                cellErrors.addAll(rowErrors);
                return false;
            }
        }
        return true;
    }

    private boolean titleVerify(int sheetIdx) {
        // 如果校验器不为空进行校验
        if (titleValidator != null) {
            return titleValidator.verify(sheetIdx);
        }
        return true;
    }

    public String[] getRowValues(int sheetIdx, int row) {
        Sheet sheet = wb.getSheetAt(sheetIdx);
        Row titleRow = sheet.getRow(row);
        return ExcelTool.getStringValues(titleRow);
    }

    public void checkDuplicate(Set<String> set, String value, ExcelDto dto, int colIdx, String msg) {
        if (!CellRule.valueIsEmpty(value)) {
            if (set.contains(value)) {
                this.addCellError(dto, colIdx, msg);
            }
            set.add(value);
        }
    }

    public void addCellError(ExcelDto dto, int col, String msg) {
        this.cellErrors.add(new CellError(wb.getSheetAt(dto.getSheet()).getRow(dto.getRow()), col, msg));
    }

    public void addCellError(int sheet, int row, int col, String msg) {
        this.cellErrors.add(new CellError(wb.getSheetAt(sheet).getRow(row), col, msg));
    }

    public boolean hasError() {
        return cellErrors.size() > 0;
    }

    public String getErrorFileName() {
        return System.currentTimeMillis() + "." + ExcelType.XLSX.name().toLowerCase();
    }

    @Override
    public void close() throws IOException {
        this.cellErrors = null;
        wb.close();
    }
}
