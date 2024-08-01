package com.focusup.global.security.jwt;
import java.io.IOException;

import com.focusup.global.apiPayload.exception.TokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import static com.focusup.global.apiPayload.code.ErrorCode.INVALID_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isLogin(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            processTokenAuthentication(request);
        } catch (TokenException e) {
            log.error("Invalid Token", e);
            throw e;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLogin(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api/user/auth"); // 토큰 검사 X
    }

    private void processTokenAuthentication(HttpServletRequest request) {
        String token = getToken(request);
        if (jwtTokenUtils.validateToken(token)) {
            Authentication authentication = jwtTokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return;
        }
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteAddr();
        }
        log.error("Invalid token for requestURI: {}, Access from IP: {}", request.getRequestURI(), clientIp);
        throw new TokenException(INVALID_TOKEN);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
