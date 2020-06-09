package cn.com.tiza.util;

import org.springframework.util.StringUtils;

/**
 * @author villas
 * @since 2019/6/13 10:01
 */
public class ToolUtil {

    private ToolUtil() {}

    public static String wrapLike(String value) {
        if (StringUtils.hasText(value)) {
            value = value.replaceAll("%", "\\\\%");
            value = value.replaceAll("_", "\\\\_");
            return "%" + value + "%";
        }
        return value;
    }

    /**
     * 计算hashMap的初始容量的
     *
     * @param value 需要放入的元素个数
     */
    public static int computeMap(int value) {
        if (value <= 0) {
            return 16;
        }
        return (int)Math.ceil(value * (4 / 3.0)) + 1;
    }
}
