package cn.com.tiza.service.dto;

import cn.com.tiza.excel.ExcelDto;
import cn.com.tiza.excel.read.CellRule;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

import static cn.com.tiza.excel.read.ExcelTool.getValue;


/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class SimcardDto extends ExcelDto {

    private Long id;
    /**
     * 办卡方式
     */
    @NotNull
    private Long cardWayId;
    /**
     * 服务状态
     */
    private Integer status;
    /**
     * 办卡客户
     */
    private String cardOwner;
    /**
     * SIM卡号
     */
    @NotBlank
    private String code;
    private Long createTime;
    /**
     * 销售订单号
     */
    private String orderNo;

    /**
     *事业部
     */
    private String department;

    /**
     *运营商（1：移动、 2：联通 、3：电信）
     */
    private Integer operator;

    public SimcardDto() {
    }

    private String operatorName;
    private String cardWayName;

    public SimcardDto(int sheet, Row row) {
        super(sheet, row.getRowNum());
        int i = 0;
        this.code = getValue(row.getCell(i++));
        this.cardOwner = getValue(row.getCell(i++));
        this.orderNo = getValue(row.getCell(i++));
        this.department = getValue(row.getCell(i++));
        this.operatorName = getValue(row.getCell(i++));
        this.cardWayName = getValue(row.getCell(i));

    }

}
