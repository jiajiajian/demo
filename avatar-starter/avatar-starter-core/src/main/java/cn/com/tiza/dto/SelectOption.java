package cn.com.tiza.dto;

import lombok.Data;

/**
 * 下拉选择公共类
 * @author tiza
 */
@Data
public class SelectOption {

    private Long id;

    private String code;

    private String name;

    public SelectOption() {
    }

    public SelectOption(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SelectOption(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public SelectOption(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
