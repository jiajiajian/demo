package cn.com.tiza.filter;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.dto.UserType;
import cn.com.tiza.web.rest.AccountApiClient;
import cn.com.tiza.web.util.Servlets;
import cn.com.tiza.web.util.TokenHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * @author tiza
 */
@Configuration
@Slf4j
public class AccessGatewayFilter implements GlobalFilter {

    private static final String PREFIX = "/api";
    private static final int PREFIXLENGTH = PREFIX.length();
    private static final String SPLIT = ",";
    private static final int BAREAR = "Barear ".length();

    @Value("${gate.ignore.startWith}")
    private String startWith;

    @Value("${sso.enabled}")
    private boolean ssoEnable;

    private String[] splitStarts;

//    @Autowired
//    private TokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountApiClient accountApi;

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, GatewayFilterChain gatewayFilterChain) {
        log.info("check token and user permission....");
        LinkedHashSet requiredAttribute = serverWebExchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        ServerHttpRequest request = serverWebExchange.getRequest();

        BaseContextHandler.set(Constants.CONTEXT_KEY_USER_IP, Servlets.getClientIP(request));
        BaseContextHandler.set(Constants.CONTEXT_KEY_USER_AGENT, Servlets.getUserAgent(request));

        String requestUri = request.getPath().pathWithinApplication().value();
        if (requiredAttribute != null) {
            Iterator<URI> iterator = requiredAttribute.iterator();
            while (iterator.hasNext()) {
                URI next = iterator.next();
                if (next.getPath().startsWith(PREFIX)) {
                    requestUri = next.getPath().substring(PREFIXLENGTH);
                }
            }
        }
        ServerHttpRequest.Builder mutate = request.mutate();
        // 不进行拦截的地址
        if (isStartWith(requestUri)) {
            ServerHttpRequest build = mutate.build();
            addEmptyUserTokenHeader(mutate);
            return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
        }
        String jwt = resolveToken(request);
        if (StringUtils.hasText(jwt)) {
//            UserInfo authentication = this.tokenProvider.getAuthentication(jwt);
            UserInfo authentication = accountApi.parseToken(jwt);
            if (authentication == null) {
                log.error("用户Token过期异常");
                return getVoidMono(serverWebExchange, "User Token Forbidden or Expired!");
            }
            String redisToken = accountApi.getTokenByLoginName(authentication.getLoginName());
            if(ssoEnable && !jwt.equals(redisToken)){
                log.error("用户在其他地方登陆");
                return getVoidMono(serverWebExchange, "User Token Login Other!");
            }
            //将用户信息传递给后续接口，由接口做权限验证
            addUserTokenHeader(authentication, mutate);
        } else {
            log.error("用户Token过期异常");
            return getVoidMono(serverWebExchange, "User Token Forbidden or Expired!");
        }
        ServerHttpRequest build = mutate.build();
        return gatewayFilterChain.filter(serverWebExchange.mutate().request(build).build());
    }

    /**
     * 将url用,分割
     */
    @PostConstruct
    public void splitStartPrefix() {
        log.debug("SPLIT start urls");
        if (startWith != null && StringUtils.hasText(startWith)) {
            this.splitStarts = startWith.split(SPLIT);
        } else {
            this.splitStarts = new String[0];
        }
    }

    private void addEmptyUserTokenHeader(ServerHttpRequest.Builder ctx) {
        log.debug("add user info to header!");
        UserInfo user = UserInfo.builder()
                .id(-1L).userType(UserType.ADMIN)
                .ipAddress(BaseContextHandler.getValue(Constants.CONTEXT_KEY_USER_IP))
                .userAgent(BaseContextHandler.getValue(Constants.CONTEXT_KEY_USER_AGENT))
                .build();
        addHeader(user, ctx);
    }

    private void addUserTokenHeader(UserInfo authentication, ServerHttpRequest.Builder ctx) {
        log.debug("add user info to header!");
        authentication.setIpAddress(BaseContextHandler.getValue(Constants.CONTEXT_KEY_USER_IP));
        authentication.setUserAgent(BaseContextHandler.getValue(Constants.CONTEXT_KEY_USER_AGENT));
        addHeader(authentication, ctx);
    }

    private void addHeader(UserInfo user, ServerHttpRequest.Builder ctx) {
        try {
            String userJson = objectMapper.writeValueAsString(user);
            ctx.header(Constants.USER_INFO_HEADER, URLEncoder.encode(userJson, "UTF-8"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isStartWith(String requestUri) {
        for (String s : this.splitStarts) {
            if (requestUri.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    private String resolveToken(ServerHttpRequest request) {
        String authToken = TokenHelper.resolveToken(request, Constants.AUTHORIZATION_HEADER, "token");
        if (StringUtils.hasText(authToken) && authToken.length() > BAREAR) {
            authToken = authToken.substring(BAREAR);
        }
        return authToken;
    }

    /**
     * 网关抛异常
     *
     * @param body
     */
    private Mono<Void> getVoidMono(ServerWebExchange serverWebExchange, String body) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(bytes);
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }
}
