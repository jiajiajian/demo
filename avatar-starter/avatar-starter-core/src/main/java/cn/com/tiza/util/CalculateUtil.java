package cn.com.tiza.util;

import java.math.BigDecimal;

/**
 * 数值计算工具类
 */
public class CalculateUtil {

    /**
     * 2个 BigDecimal 数值相加
     * @param a
     * @param b
     * @return
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if(a == null) {
            return b;
        }
        if(b == null) {
            return a;
        }
        return a.add(b);
    }


    /* long  */

    /**
     * 2个long 数值相加
     * @param a
     * @param b
     * @return
     */
    public static Long add(Long a, Long b) {
        if(a == null) {
            return b;
        }
        if(b == null) {
            return a;
        }
        return a + b;
    }
}
