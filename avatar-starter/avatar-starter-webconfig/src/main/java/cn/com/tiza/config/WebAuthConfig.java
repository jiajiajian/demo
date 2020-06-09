package cn.com.tiza.config;

import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.BaseContextHandler;
import cn.com.tiza.context.UserInfo;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.List;

/**
 * @author tiza
 */
@Slf4j
@Configuration
public class WebAuthConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
    }

    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                    throws Exception {
                String token = request.getHeader(Constants.USER_INFO_HEADER);
                if (token != null) {
                    token = URLDecoder.decode(token, "UTF-8");
                    BaseContextHandler.setToken(token);
                    UserInfo user = JsonMapper.defaultMapper().fromJson(token, UserInfo.class);
                    log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~addInterceptors user info is {}", user);
                    if (user != null) {
                        BaseContextHandler.setUser(user);
                        BaseContextHandler.setUserID(user.getId());
                        BaseContextHandler.setUserType(user.getUserType());
                        if (user.isOrganization()) {
                            BaseContextHandler.setRootOrgId(user.getRootOrgId());
                            BaseContextHandler.setOrgId(user.getOrgId());
                        } else if (user.isFinance()) {
                            BaseContextHandler.setFinanceId(user.getFinanceId());
                        } else {
                            BaseContextHandler.setRootOrgId(null);
                            BaseContextHandler.setOrgId(null);
                            BaseContextHandler.setFinanceId(null);
                        }
                        BaseContextHandler.setLoginName(user.getLoginName());
                        BaseContextHandler.setName(user.getRealName());
                        BaseContextHandler.setIPAddress(user.getIpAddress());
                        BaseContextHandler.setUserAgent(user.getUserAgent());
                    } else {
                        BaseContextHandler.remove();
                    }
                } else {
                    BaseContextHandler.remove();
                }
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                BaseContextHandler.remove();
            }
        });
    }

}
