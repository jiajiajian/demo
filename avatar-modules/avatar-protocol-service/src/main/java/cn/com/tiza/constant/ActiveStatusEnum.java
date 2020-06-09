package cn.com.tiza.constant;

import lombok.Getter;

/**
 * 配置表fwp_task的启动状态
 * 
 * @author villas
 * @since 2019/4/30 10:47
 */
@Getter
public enum ActiveStatusEnum {

    ACTIVE("已启动", 1), INACTIVE("未启动", 0);

    private String msg;

    private Integer value;

    private ActiveStatusEnum(String msg, Integer value) {
        this.msg = msg;
        this.value = value;
    }

}
