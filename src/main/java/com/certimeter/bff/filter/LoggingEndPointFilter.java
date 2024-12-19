package com.certimeter.bff.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class LoggingEndPointFilter extends OncePerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(LoggingEndPointFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        LOG.info("--------- START - {} {}?{} ------", wrappedRequest.getMethod(), wrappedRequest.getRequestURI(), wrappedRequest.getQueryString());
        LOG.info("Endpoint: {}", wrappedRequest.getRequestURI());
        LOG.info("Method: {}", wrappedRequest.getMethod());
        LOG.info("Query string: {}", wrappedRequest.getQueryString());

        filterChain.doFilter(wrappedRequest, response);

        String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
        if (!requestBody.isEmpty()) {
            LOG.info("Request body: {}", requestBody);
        }
        LOG.info("---------- END - {} {} ------", wrappedRequest.getMethod(), wrappedRequest.getRequestURI());
    }
}