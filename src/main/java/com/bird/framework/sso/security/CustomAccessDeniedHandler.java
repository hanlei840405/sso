package com.bird.framework.sso.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    // NoLogged Access Denied
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", authException.getMessage());
        SecurityWriter.write(response, objectMapper.writeValueAsString(result));
    }

    // Logged Access Denied
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 403);
        result.put("message", accessDeniedException.getMessage());
        SecurityWriter.write(response, objectMapper.writeValueAsString(result));
    }
}
