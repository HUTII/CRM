package org.example.crm.config;

import jakarta.annotation.Resource;
import org.example.crm.interceptor.JwtInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * @author hutianlin
 * 2024/11/10 19:03
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);
    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("加载JwtInterceptor配置，排除路径 /user/login");
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/error");
    }
}
