package com.example.filterandlogging.common.auth;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
public class XSSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 필터 진입 확인
        log.info("===== XSS 필터 시작 =====");
        log.info("요청 URI: {}", httpServletRequest.getRequestURI());
        log.info("요청 방식: {}", httpServletRequest.getMethod());
        log.info("Content-Type: {}", httpServletRequest.getContentType());

        XSSFilterWrapper wrappedRequest = new XSSFilterWrapper(httpServletRequest);
        log.info("===== XSS 필터 끝 =====");
        chain.doFilter(wrappedRequest, response);
    }
}
