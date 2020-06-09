package cn.com.tiza.security.auth.jwt;

import cn.com.tiza.context.UserInfo;
import cn.com.tiza.security.auth.StringHelper;
import cn.com.tiza.security.auth.config.KeyConfiguration;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

/**
 * @author tiza
 */
@Slf4j
@Component
public class TokenProvider {

    private static final String JWT_KEY_USER = "user";

    private Key key;

    private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;

    private final KeyConfiguration keyConfiguration;

    public TokenProvider(KeyConfiguration keyConfiguration) {
        this.keyConfiguration = keyConfiguration;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        String secret = keyConfiguration.getSecret();
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded. " +
                    "We recommend using the `jwt.base64-secret` key for optimum security.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            log.debug("Using a Base64-encoded JWT secret key");
            keyBytes = Decoders.BASE64.decode(keyConfiguration.getBase64Secret());
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds =
                1000 * keyConfiguration.getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe =
                1000 * keyConfiguration.getTokenValidityInSecondsForRememberMe();
    }

    /**
     * 创建一个短期的访问Token
     *
     * @param user user
     * @return
     */
    public String createAccessToken(UserInfo user, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        return Jwts.builder()
                .setSubject(user.getLoginName())
                .claim(JWT_KEY_USER, user.toJson())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 创建一个刷新Token
     *
     * @param jwtInfo
     * @param rememberMe
     * @return
     */
    public String createRefreshToken(UserInfo jwtInfo, boolean rememberMe) {
        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }
        return Jwts.builder()
                .setSubject(jwtInfo.getLoginName())
                .claim("rememberMe", rememberMe ? "1" : "0")
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }

    public UserInfo getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return JsonMapper.defaultMapper().fromJson((String) claims.get(JWT_KEY_USER), UserInfo.class) ;
    }

    public JWTRememberMe getRememberMe(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return new JWTRememberMe(claims.getSubject(), StringHelper.getObjectValue(claims.get("rememberMe")));
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
}
