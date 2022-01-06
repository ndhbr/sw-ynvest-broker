package de.ndhbr.configuration;

import de.ndhbr.interceptor.AlertInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConf implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AlertInterceptor()).excludePathPatterns(
                SecurityConf.STATIC_PATHS
        );
    }
}

