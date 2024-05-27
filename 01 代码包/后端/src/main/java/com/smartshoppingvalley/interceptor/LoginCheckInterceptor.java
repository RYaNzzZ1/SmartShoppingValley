package com.smartshoppingvalley.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        log.info("LoginCheckInterceptor");
        log.info(request.getRequestURI());
        return true;
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements())
//            log.info(headerNames.nextElement());

//        //用户名
//        String id = null;
//
//        try {
//            // 获取请求中的 Token
//            String token = GetToken.getTokenFromRequest(request);
//
//            log.info(token);
//
//            if (token == null) {
//                response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
//                return false;
//            }
//
//            if (JwtTokenProvider.validateToken(token)) {
//                // 从 Token 中获取用户信息并设置认证
//                id = GetToken.getIdFromToken(token);
//                log.info("用户id：{}", id);
//
//            }
//        } catch (ExpiredJwtException expiredJwtException) {
//            // 处理 Token 过期异常
//        }
//
//        //用户名为空
//        //if (id != null
//
//
////        response.getWriter().write(JSON.toJSONString(Response.error(501,"token过期")));
////        return false;
    }
}
