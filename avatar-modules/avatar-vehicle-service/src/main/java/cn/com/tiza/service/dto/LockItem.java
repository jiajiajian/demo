package cn.com.tiza.service.dto;

import cn.com.tiza.domain.BaseEnum;
import org.beetl.sql.core.annotatoin.EnumMapping;

/**
 * 锁车种类
 *
 * @author tz0920
 */

public enum LockItem {
    /**
     * 继电器一级锁车
     */
    RELAY_LOCK_1("RELAY_LOCK_1","继电器一级锁车"),
    /**
     * 继电器一级解锁
     */
    RELAY_UNLOCK_1("RELAY_UNLOCK_1","继电器一级解锁"),
    /**
     * 继电器二级锁车
     */
    RELAY_LOCK_2("RELAY_LOCK_2","继电器二级锁车"),
    /**
     * 继电器二级解锁
     */
    RELAY_UNLOCK_2("RELAY_UNLOCK_2","继电器二级解锁"),
    /**
     * 继电器三级锁车
     */
    RELAY_LOCK_3("RELAY_LOCK_3","继电器三级锁车"),
    /**
     * 继电器三级锁车
     */
    RELAY_UNLOCK_3("RELAY_UNLOCK_3","继电器三级锁车"),
    /**
     * 控制器锁车
     */
    CONTROLLER_LOCK("CONTROLLER_LOCK","控制器锁车"),
    /**
     * 控制器解锁
     */
    CONTROLLER_UNLOCK("CONTROLLER_UNLOCK","控制器解锁");

    LockItem(String name,String value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private String value;


    public void setName(String name) {
        this.name = name;
    }

    public static String getValue(String name) {
        for (LockItem status : LockItem.values()) {
            if (status.name == name) {
                return status.value;
            }
        }
        return null;
    }
}
