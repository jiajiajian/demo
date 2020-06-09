package cn.com.tiza.service.dto;

import cn.com.tiza.dto.Query;
import lombok.Getter;
import lombok.Setter;

/**
* 远程锁车参数
* @author xiaotao
*/
@Getter
@Setter
public class LockQuery extends Query {

    /**
     * 组织ID
     */
    private Long orgId;
    /**
     * 执行指令
     */
    private Long orderId;
    /**
     * 执行状态
     */
    private Integer runState;
    /**
     * 机器序列号/终端编号/SIM卡号
     */
    private String keywords;

    private Long vehicleTypeId;
    private Long vehicleModelId;

    @Override
    protected void convertParams() {
        add("orgId", this.orgId);
        add("orderId", this.orderId);
        add("runState", this.runState);
        add("keywords", this.keywords, true);
        add("vehicleTypeId", this.vehicleTypeId);
        add("vehicleModelId", this.vehicleModelId);
    }

}
