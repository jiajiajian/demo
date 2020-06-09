package cn.com.tiza.excel;

import org.apache.commons.lang3.Validate;
import org.apache.poi.hssf.usermodel.HSSFCreationHelper;
import org.apache.poi.ss.usermodel.*;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class POIUtil {

    public static void setComment(CreationHelper factory, Drawing drawing, Cell cell, String message) {
        Validate.notNull(cell, "单元格不能为null");
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

    public static void appendComment(CreationHelper factory, Drawing drawing, Cell cell, String message) {
        Validate.notNull(cell, "单元格不能为null");
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

    /**
     * 生成excel导出头 需要在ExcelDownloadConstants文件中配置
     *
     * @param object excel定义文件类
     */
    public static LinkedHashMap<String, Object> getExportHeader(Object object) throws IllegalAccessException {

        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            map.put(f.getName(), f.get(object));
        }
        return map;
    }

}
