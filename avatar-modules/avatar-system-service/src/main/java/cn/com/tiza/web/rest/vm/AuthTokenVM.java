package cn.com.tiza.web.rest.vm;

import lombok.Data;

/**
 * 认证token
 * @author tiza
 */
@Data
public class AuthTokenVM {

    /**
     * 用户认证token
     */
    private String accessToken;
    private String refreshToken;
    /**
     * WebSocket 认证Token,有效期和accessToken一致(用户刷新页面ws session会重置)
     */
    private String wsToken;

    public AuthTokenVM() {}

    public AuthTokenVM(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
