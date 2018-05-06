package com.bird.framework.sso.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomLoginHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    // Login Success
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        SecurityWriter.write(response, objectMapper.writeValueAsString(result));
    }

    // Login Failure
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 401);
        result.put("message", exception.getMessage());
        SecurityWriter.write(response, objectMapper.writeValueAsString(result));
    }
}
