package com.nyoongoon.fullstackjava.config;

import com.nyoongoon.fullstackjava.config.data.UserSession;
import com.nyoongoon.fullstackjava.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
       return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        String accessToken = webRequest.getParameter("accessToken");
        // getParameter 형태로 가져오면 다른 정보와 충돌이 될 수 있기 떄문에 헤더 사용!
        String accessToken = webRequest.getHeader("Authorization");
        if(accessToken == null || accessToken.equals("")){
            throw new Unauthorized();
        }
        // DB 사용자 확인 작업
        // ...

        return new UserSession(1L); // 디비 넘어온값 인자로 추후 넣기
    }
}
