package com.nyoongoon.fullstackjava.config;

import com.nyoongoon.fullstackjava.config.data.UserSession;
import com.nyoongoon.fullstackjava.domain.Session;
import com.nyoongoon.fullstackjava.exception.Unauthorized;
import com.nyoongoon.fullstackjava.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
       return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        String accessToken = webRequest.getParameter("accessToken");
        // getParameter 형태로 가져오면 다른 정보와 충돌이 될 수 있기 떄문에 헤더 사용!
//        String accessToken = webRequest.getHeader("Authorization"); => 쿠키로 변경
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if(servletRequest == null){
            log.error("servletRequest null");
            throw new Unauthorized();
        }

        Cookie[] cookies = servletRequest.getCookies();
        if(cookies.length == 0){
            log.error("쿠키가 없음");
            throw new Unauthorized();
        }
        // DB 사용자 확인 작업
        // ...
        String accessToken = cookies[0].getValue();
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        return new UserSession(session.getUser().getId());
    }
}
