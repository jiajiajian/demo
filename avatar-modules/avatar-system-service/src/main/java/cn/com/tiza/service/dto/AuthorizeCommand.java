package cn.com.tiza.service.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 角色授权接受类
 */
@Data
public class AuthorizeCommand {
    private Long id;
    private Integer checkStatus;

    public AuthorizeCommand() {
    }

    public AuthorizeCommand(Long id, Integer checkStatus) {
        this.id = id;
        this.checkStatus = checkStatus;
    }
}
