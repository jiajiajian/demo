package cn.com.tiza.domain;

import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 指令状态
 */

@EnumMapping("value")
public enum OrderStatus implements BaseEnum{

    //执行状态
    /**
     * 失败
     */
    FAILURE("成功",0),
    /**
     * 成功
     */
    SUCCESS("失败",1),
    /**
     * 超时
     */
    TIME_OUT("超时",2),
    /**
     * 执行中
     */
    IN_PROGRESS("执行中",3),
    /**
     * 离线指令已发送
     */
    OFFLINE_SEND("离线指令已发送",4)
    ;

    OrderStatus(String name, int value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private int value;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getValue() {
        return value;
    }

    public static OrderStatus fromValue(int value) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.value == value) {
                return orderStatus;
            }
        }
        return null;
    }
}
