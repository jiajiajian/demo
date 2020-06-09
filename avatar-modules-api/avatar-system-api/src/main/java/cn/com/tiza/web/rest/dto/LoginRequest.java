package cn.com.tiza.web.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    private String patchca;

    private String timestamp;

    private boolean rememberMe = false;
}
