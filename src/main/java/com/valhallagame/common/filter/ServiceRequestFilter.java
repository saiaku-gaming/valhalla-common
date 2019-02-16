package com.valhallagame.common.filter;

import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ServiceRequestFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;

            if(MDC.getMDCAdapter() != null && request.getHeader("X-REQUEST-ID") != null) {
                MDC.put("requestId", request.getHeader("X-REQUEST-ID"));
            }

            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            if(MDC.getMDCAdapter() != null) {
                MDC.clear();
            }
        }
    }
}
