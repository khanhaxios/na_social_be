package com.NA.social.core.ultis;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class AccessDeinedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AccessDeinedModel jsonResponse = new AccessDeinedModel();
            mapper.writeValue(response.getOutputStream(), jsonResponse);
        } catch (Exception e) {
            throw new ServletException();
        }
    }

    @Data
    public static class AccessDeinedModel {
        private int code = 1129;
        private String message = "AccessDenied";
    }
}