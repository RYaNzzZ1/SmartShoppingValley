//package com.smartshoppingvalley.filter;
//
//import cn.hutool.http.HttpStatus;
//import com.alibaba.fastjson.JSON;
//import com.smartshoppingvalley.tools.GetToken;
//import com.smartshoppingvalley.tools.JwtTokenProvider;
//import com.smartshoppingvalley.tools.Result;
//import io.jsonwebtoken.ExpiredJwtException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.net.ssl.SSLEngineResult;
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Enumeration;
//
//@Component
//@Slf4j
//@WebFilter("/*")
////public class JwtAuthenticationFilter extends OncePerRequestFilter {
////
////    //路径匹配，支持通配符
////    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
////
////    //不需要处理的请求路径
////    private static final String[] uris = {
////            "/Admin/admin/login",
////    };
////
////    @Override
////    protected void doFilterInternal(
////            HttpServletRequest request,
////            HttpServletResponse response,
////            FilterChain filterChain
////    ) throws ServletException, IOException {
////
//
//
////        String uri = request.getRequestURI();
////        log.info("拦截到请求：{}", uri);
////
////        //不需要处理的请求路径
////        for (String s : uris) {
////            if (antPathMatcher.match(s, uri)) {
////                //放行
////                filterChain.doFilter(request, response);
////                return;
////            }
////        }
////
////        //用户名
////        String username = null;
////
////        try {
////            // 获取请求中的 Token
////            String token = GetToken.getTokenFromRequest(request);
////
////            //log.info(token);
////
////            if (token != null && jwtTokenProvider.validateToken(token)) {
////                // 从 Token 中获取用户信息并设置认证
////                username = GetToken.getUsernameFromToken(token);
////
////            }
////        } catch (ExpiredJwtException expiredJwtException) {
////            // 处理 Token 过期异常
////        }
////
////        //用户名为空
////        if (username != null)
////            filterChain.doFilter(request, response);
////
////        //log.info(username);
////
////        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
////    }
//
////    }
////}
//
//public class JwtAuthenticationFilter implements Filter {
//
//    //    路径匹配，支持通配符
//    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//    //不需要处理的请求路径
//    private static final String[] uris = {
//            "/Admin/admin/login",
//    };
//
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        log.info("filter");
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        String uri = request.getRequestURI();
//        log.info("拦截到请求：{}", uri);
//
//        //不需要处理的请求路径
//        for (String s : uris) {
//            if (antPathMatcher.match(s, uri)) {
//                //放行
//                filterChain.doFilter(request, response);
//                return;
//            }
//        }
//
//        response.setHeader("Access-control-Allow-Origin", request.getHeader("Origin"));
//
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,token");
//        // 暴露token，这里一定要写，如果你的写法与我的相同，检查此行代码是否也添加了
//        response.setHeader("Access-Control-Expose-Headers", "token");
//        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//
//        if ("OPTIONS".equals(request.getMethod())) {
//            log.info("OPTIONS");
//            response.setStatus(HttpStatus.HTTP_ACCEPTED);
//            return;
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}