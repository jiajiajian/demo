package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimcardQuery extends Query {

    /**
     * SIM卡号
     */
    private String code;
    /**
     * 办卡客户
     */
    private String cardOwner;

    /**
     * 销售订单号
     */
    private String orderNo;

    /**
     * 0：未分配，1：已分配，2：暂停，3：到期，4：预销户，5：废弃
     * 服务状态
     */
    private Integer status;

    /**
     * 办卡方式
     */
    private Long cardWayId;

    private String department;

    private Integer operator;

    /**
     * 卡种类 ：11代表11位卡，13代表13位卡
     */
    private Integer cardType;

    @Override
    protected void convertParams() {
         add("code", this.code, true);
         add("cardOwner", this.cardOwner, true);
         add("orderNo", this.orderNo, true);
         add("status", this.status);
         add("cardWayId", this.cardWayId);
         add("department", this.department, true);
         add("operator", this.operator);
         add("cardType", this.cardType);
    }

}
