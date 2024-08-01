package com.focusup.domain.user.controller;

import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.domain.user.dto.RefreshTokenRequest;
import com.focusup.domain.user.service.UserServiceImpl;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.security.jwt.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/test")
    public Response<String> test(Authentication authentication) {
        return Response.success(authentication.getName());
    }

    @GetMapping("/auth/success")
    @Operation(summary = "로그인 성공 후 토큰 전송, 내부 api, 클라이언트 사용 X")
    public Response<LoginResponse> loginSuccess(@Valid LoginResponse loginResponse) {
        return Response.success(loginResponse);
    }

    @PostMapping("/auth/reissue")
    @Operation(summary = "access token 재발급 api")
    public Response<TokenInfo> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return Response.success(userServiceImpl.refreshAccessToken(request.getRefreshToken()));
    }

    @GetMapping("/auth/kakao")
    @Operation(summary = "Web 소셜 로그인 용 api")
    public RedirectView kakaoLogin() {
        return new RedirectView("/oauth2/authorization/kakao");
    }
}
