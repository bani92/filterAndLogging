package com.example.filterandlogging.common.configs;

import com.example.filterandlogging.common.auth.XSSFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class XSSFilterConfig {
//
//    @Bean
//    public FilterRegistrationBean<XSSFilter> xssFilterRegistration() {
//        FilterRegistrationBean<XSSFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new XSSFilter());
//        registrationBean.setOrder(0);
//        registrationBean.addUrlPatterns("/*");
//        return registrationBean;
//    }
//}
