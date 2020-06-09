package cn.com.tiza.web.vm;


import lombok.Data;

/**
 * gen by beetlsql 2020-03-09
 *
 * @author tiza
 */
@Data
public class SimcardVM {

    private Long id;
    /**
     * 办卡方式
     */
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

    public SimcardVM() {
    }

    /*******非数据库字段*******/

    private String carWay;

    private Integer serviceStartDate;

    private Integer serviceEndDate;

}
