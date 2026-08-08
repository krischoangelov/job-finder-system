package com.jobfinder.interviewschedulingservice.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class APIKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final String X_API_KEY = "X-API-Key";

    @Value("${interview.service.api-key}")
    private String validAPIKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String APIKey = request.getHeader(X_API_KEY);

            if (APIKey == null || APIKey.isBlank()) {
                throw new RuntimeException("Missing API key header!");
            }

            if (!APIKey.equals(validAPIKey)) {
                throw new RuntimeException("Invalid API key!");
            }

            Authentication authentication = new APIKeyAuthentication(APIKey);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (RuntimeException e) {
//            response.setStatus(e.getHttpStatus().value());
            response.getWriter().write(e.getMessage());
        }

    }
}
