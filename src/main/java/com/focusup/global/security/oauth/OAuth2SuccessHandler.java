package com.focusup.global.security.oauth;

import com.focusup.global.security.jwt.JwtTokenUtils;
import com.focusup.global.security.jwt.TokenInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenUtils jwtTokenUtils;
    private final CustomOAuth2UserService customOAuth2UserService;
    private static final String URI = "/api/user/auth/success";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken, refreshToken 발급
        TokenInfo tokenInfo = jwtTokenUtils.generateToken(authentication);

        // refreshToken 저장
        customOAuth2UserService.setRefreshToken(authentication, tokenInfo.getRefreshToken());

        // 토큰 전달을 위한 redirect
        String redirectUrl = UriComponentsBuilder.fromUriString(URI)
                .queryParam("refreshToken", tokenInfo.getRefreshToken())
                .queryParam("accessToken", tokenInfo.getAccessToken())
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}