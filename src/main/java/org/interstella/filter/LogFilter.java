package org.interstella.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class LogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String requestId = UUID.randomUUID().toString();
            MDC.put("requestId", requestId);

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(httpRequest);
            ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(httpResponse);

            log.info("======= HTTP Request =======");
            log.info("Request URL: {}", wrappedRequest.getRequestURL());
            log.info("Request Method: {}", wrappedRequest.getMethod());

            logRequestParameters(httpRequest);

            chain.doFilter(request, response);

            log.info("======= HTTP Response =======");
            log.info("Response Status: {}", wrappedResponse.getStatus());

        } finally {
            MDC.remove("requestId");
        }
    }

    private void logRequestParameters(HttpServletRequest request) {
        request.getParameterMap().forEach((key, value) -> {
            String paramName = key;
            String paramValue = String.join(",", value);
            log.info("Request Parameters, {}: {}", paramName, paramValue);
        });
    }
}