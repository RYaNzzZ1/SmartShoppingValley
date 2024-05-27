package com.smartshoppingvalley.config;

import com.smartshoppingvalley.interceptor.LoginCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/Admin/admin/login").excludePathPatterns("/Customer/customer/sendCode")
                .excludePathPatterns("/Customer/customer/register").excludePathPatterns("/Customer/customer/loginByPW")
                .excludePathPatterns("/Customer/customer/loginByCode").excludePathPatterns("/ImageFile/image/download")
                .excludePathPatterns("/Commodity/type/getTypeByFatherId").excludePathPatterns("/Commodity/commodity/*")
                .excludePathPatterns("/Comments/comments/getAverageScore").excludePathPatterns("/Comments/comments/pageByCommodityId")
                .excludePathPatterns("/ImageFile/image/upload");
    }
}
