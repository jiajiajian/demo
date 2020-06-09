package cn.com.tiza.config;

import cn.com.tiza.annotation.CurrentUser;
import cn.com.tiza.constant.Constants;
import cn.com.tiza.context.UserInfo;
import cn.com.tiza.web.rest.errors.BadRequestException;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.net.URLDecoder;

/**
 * 登录用户信息
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //如果参数类型是User并且有CurrentUser注解则支持
        if (methodParameter.getParameterType().isAssignableFrom(UserInfo.class) &&
                methodParameter.hasParameterAnnotation(CurrentUser.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //取出鉴权时存入的登录用户
        String token = nativeWebRequest.getHeader(Constants.USER_INFO_HEADER);
        if (token != null) {
            token = URLDecoder.decode(token, "UTF-8");
            return JsonMapper.defaultMapper().fromJson(token, UserInfo.class);
        }
        throw new BadRequestException("403");
    }
}
