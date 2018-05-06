package com.bird.framework.sso.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomLogoutHandler implements LogoutHandler, LogoutSuccessHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    // Before Logout
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    }

    // After Logout
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登出成功");
        SecurityWriter.write(response, objectMapper.writeValueAsString(result));
    }
}
