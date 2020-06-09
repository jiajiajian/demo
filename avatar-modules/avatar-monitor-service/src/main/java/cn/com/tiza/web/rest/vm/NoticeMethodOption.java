package cn.com.tiza.web.rest.vm;

import cn.com.tiza.service.dto.NoticeMethod;
import lombok.Data;

/**
 * @author TZ0781
 */
@Data
public class NoticeMethodOption {

    private String name;

    private String label;

    public NoticeMethodOption() {
    }

    public NoticeMethodOption(NoticeMethod method) {
        this.name = method.name();
        this.label = method.getLabel();
    }
}
