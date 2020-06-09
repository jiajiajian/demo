package cn.com.tiza.service.dto;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TZ0781
 */
public enum NoticeMethod {

    /**
     * 不通知
     */
    NON("不通知"),
    /**
     * 短信通知
     */
    SMS("短信"),
    /**
     * 邮件通知
     */
    EMAIL("邮件");

    private String label;

    NoticeMethod(String label) {
        this.label = label;
    }

    public static List<NoticeMethod> parse(String remindWay) {
        List<NoticeMethod> methods = new ArrayList<>();
        if(StringUtils.hasText(remindWay)) {
            String[] arr = remindWay.split(",");
            for (String s : arr) {
                if(StringUtils.hasText(s)) {
                    methods.add(NoticeMethod.valueOf(s));
                }
            }
        }
        return methods;
    }

    public static boolean isNon(List<NoticeMethod> methods) {
        for (NoticeMethod method : methods) {
            if(NON.equals(method)) {
                return true;
            }
        }
        return false;
    }

    public String getLabel() {
        return label;
    }
}
