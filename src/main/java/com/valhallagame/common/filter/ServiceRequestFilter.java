package com.valhallagame.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class ServiceRequestFilter extends GenericFilterBean {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRequestFilter.class);

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            if(MDC.getMDCAdapter() != null)
            {
                MDC.put("service_name", appName);
                String clientIp = request.getHeader("X-FORWARDED-FOR");
                MDC.put("request_ip", clientIp != null ? clientIp : request.getRemoteHost());
                if(request.getHeader("X-REQUEST-ID") != null) {
                    MDC.put("request_id", request.getHeader("X-REQUEST-ID"));
                } else {
                    MDC.put("request_id", UUID.randomUUID().toString());
                }
            }

            logger.info("Received {} call on {}", request.getMethod(), request.getRequestURI());
            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            if(MDC.getMDCAdapter() != null) {
                MDC.clear();
            }
        }
    }
}
