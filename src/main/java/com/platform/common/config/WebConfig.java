package com.platform.common.config;

import com.platform.common.filter.FilterUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("#{'${filter.while-url}'.split(',')}")
    private List<String> whileUrl;

    @Autowired
    private FilterUtils filterUtils;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(filterUtils).excludePathPatterns(whileUrl);
    }
}
