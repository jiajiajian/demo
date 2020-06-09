package cn.com.tiza.security.auth.jwt;

import lombok.Getter;

@Getter
public class JWTRememberMe {

    private final String userId;

    private final boolean rememberMe;

    public JWTRememberMe(String subject, String rememberMe) {
        this.userId = subject;
        this.rememberMe = "1".equalsIgnoreCase(rememberMe);
    }

}
