package com.focusup.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.apiPayload.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (TokenException e) {
            sendErrorResponse(response, e.getErrorCode().getHttpStatus().value(), e.getMessage());
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        Response<Void> body = Response.error(message);
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = objectMapper.writeValueAsString(body);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}