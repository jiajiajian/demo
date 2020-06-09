package cn.com.tiza.web.util;

import cn.com.tiza.constant.Constants;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;

/**
 * JWT认证辅助类
 *
 * @author TZ0781
 */
public class TokenHelper {

    private static final int BAREAR = "Barear ".length();

    /**
     * 从Header或者url中提取认证信息
     *
     * @param request
     * @return
     */
    public static String resolveToken(ServerHttpRequest request) {
        String authToken = request.getHeaders().getFirst(Constants.AUTHORIZATION_HEADER);
        if (StringUtils.isEmpty(authToken)) {
            authToken = request.getQueryParams().getFirst("token");
        }
        if (StringUtils.hasText(authToken) && authToken.length() > BAREAR) {
            authToken = authToken.substring(BAREAR);
        }
        return authToken;
    }

}
