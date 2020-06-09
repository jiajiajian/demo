package cn.com.tiza.domain;

import lombok.Data;
import org.beetl.sql.core.annotatoin.AssignID;
import org.beetl.sql.core.annotatoin.AutoID;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;

/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
@Table(name = "t_terminal")
public class Terminal implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 生产日期
     */
    private Long produceDate;
    /**
     * 终端编号
     */
    private String code;
    private Long createTime;
    private String createUserAccount;
    /**
     * 硬件版本
     */
    private String firmWireVersion;
    /**
     * 通信协议
     */
    private Long protocolId;
    /**
     * SIM卡ID
     */
    private Long simcardId;
    /**
     * 软件版本ID
     */
    private Long softVersionId;
    /**
     * 终端型号
     */
    private String terminalModel;

    /*******非数据库字段*******/
    /**
     * SIM卡号
     */
    private String simCode;
    /**
     * 软件版本
     */
    private String softName;
    /**
     *采集功能集
     */
    private Long collectFunctionId;
    /**
     * 锁车功能集
     */
    private Long lockFunctionId;
    /**
     * 通信协议名称
     */
    private String protocolName;

    public Terminal() {
    }

    /**
     * 格式化日期
     */
    private String createTimeFormat;

    private String produceDateFormat;

    private String collectName;

    private String lockName;
}
