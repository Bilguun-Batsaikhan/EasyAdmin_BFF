package com.certimeter.bff.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class LoggingEndPointFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingEndPointFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LOG.info("--------- START - {} {}?{} ------", request.getMethod(), request.getRequestURI(), request.getQueryString());
        LOG.info("Endpoint: {}", request.getRequestURI());
        LOG.info("Method: {}", request.getMethod());
        LOG.info("Query string: {}", request.getQueryString());
        filterChain.doFilter(request, response);
        LOG.info("---------- END - {} {} ------", request.getMethod(), request.getRequestURI());
    }
}