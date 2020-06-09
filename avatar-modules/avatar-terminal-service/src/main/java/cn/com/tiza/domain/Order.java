package cn.com.tiza.domain;

import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 执行指令
 */

@EnumMapping("value")
public enum Order implements BaseEnum{
    // 执行指令
    /**
     * 继电器一级锁车
     */
    RELAY_LOCK_1("继电器一级锁车",0),
    /**
     * 继电器一级解锁
     */
    RELAY_UNLOCK_1("继电器一级解锁",1),
    /**
     * 继电器二级锁车
     */
    RELAY_LOCK_2("继电器二级锁车",2),
    /**
     * 继电器二级解锁
     */
    RELAY_UNLOCK_2("继电器二级解锁",3),

    /**
     * 控制器锁车
     */
    CONTROLLER_LOCK("控制器锁车",4),
    /**
     * 控制器解锁
     */
    CONTROLLER_UNLOCK("控制器解锁",5)

    ;

    Order(String name, int value) {
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

    public static Order fromValue(int value) {
        for (Order order : Order.values()) {
            if (order.value == value) {
                return order;
            }
        }
        return null;
    }
}
