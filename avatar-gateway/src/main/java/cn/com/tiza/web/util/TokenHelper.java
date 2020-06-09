package cn.com.tiza.web.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

public final class TokenHelper {

    private TokenHelper() {}

    public static String resolveToken(ServerHttpRequest request, String headerName, String paramName) {
        String authToken = request.getHeaders().getFirst(headerName);
        if (StringUtils.isEmpty(authToken)) {
            authToken = request.getQueryParams().getFirst(paramName);
        }
        return authToken;
    }


}
