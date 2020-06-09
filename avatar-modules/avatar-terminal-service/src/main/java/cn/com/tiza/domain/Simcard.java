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
@Table(name = "t_simcard")
public class Simcard implements Serializable {

    @AutoID
    @AssignID("simple")
    private Long id;
    /**
     * 办卡方式
     */
    private Long cardWayId;
    /**
     * 0：未分配，1：已分配，2：暂停，3：到期，4：预销户，5：废弃
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

    /*******非数据库字段*******/

    private String cardWay;

    private Integer serviceStartDate;

    private String serviceStartDateFormat;

    private Integer serviceEndDate;

    private String serviceEndDateFormat;

    private String createTimeFormat;

    private String serviceState;

}
