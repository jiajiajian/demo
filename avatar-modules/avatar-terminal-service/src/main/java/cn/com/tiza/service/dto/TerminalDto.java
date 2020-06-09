package cn.com.tiza.service.dto;

import cn.com.tiza.excel.ExcelDto;
import lombok.Data;
import org.apache.poi.ss.usermodel.Row;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static cn.com.tiza.excel.read.ExcelTool.getValue;


/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class TerminalDto  extends ExcelDto {

    private Long id;
    /**
     * 生产日期
     */
    private Long produceDate;
    /**
     * 终端编号
     */
    @NotBlank
    private String code;
    private Long createTime;
    private String createUserAccount;
    /**
     * 硬件版本
     */
    @NotBlank
    private String firmWireVersion;
    /**
     * 通信协议
     */
    @NotNull
    private Long protocolId;
    /**
     * SIM卡ID
     */
    private Long simcardId;
    /**
     * 软件版本ID
     */
    @NotNull
    private Long softVersionId;
    /**
     * 终端型号
     */
    @NotBlank
    private String terminalModel;

    @NotNull
    private Long organizationId;

    private String vin;

    public TerminalDto() {
    }

    @NotNull
    private String simCode;
    private String softwareVersion;
    private String protocolName;
    private String orgName;

    public TerminalDto(int sheet, Row row) {
        super(sheet, row.getRowNum());
        int i = 0;
        this.code = getValue(row.getCell(i++));
        this.simCode = getValue(row.getCell(i++));
        this.softwareVersion = getValue(row.getCell(i++));
        this.protocolName = getValue(row.getCell(i++));
        this.firmWireVersion = getValue(row.getCell(i++));
        this.terminalModel = getValue(row.getCell(i++));
        this.orgName = getValue(row.getCell(i));
    }
}
