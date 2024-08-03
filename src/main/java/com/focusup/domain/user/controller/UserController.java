package com.focusup.domain.user.controller;

import com.focusup.domain.user.dto.LoginRequest;
import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.domain.user.dto.RefreshTokenRequest;
import com.focusup.domain.user.service.UserService;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.security.jwt.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/login")
    @Operation(summary = "소셜 로그인 token 전달 api")
    public Response<LoginResponse> socialLogin(@RequestBody @Valid LoginRequest request){
        return  Response.success(userService.socialLogin(request));
    }

    @PostMapping("/auth/reissue")
    @Operation(summary = "access token 재발급 api")
    public Response<TokenInfo> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return Response.success(userService.refreshAccessToken(request.getRefreshToken()));
    }

    @GetMapping("/auth/success")
    @Operation(summary = "로그인 성공 후 토큰 응답 api, 백엔드용")
    public Response<LoginResponse> loginSuccess(@Valid LoginResponse loginResponse) {
        return Response.success(loginResponse);
    }

    @GetMapping("/auth/kakao")
    @Operation(summary = "Kakao Web 소셜 로그인 용 api, 백엔드용")
    public RedirectView kakaoLogin() {
        return new RedirectView("/oauth2/authorization/kakao");
    }

    @GetMapping("/auth/naver")
    @Operation(summary = "Naver Web 소셜 로그인 용 api, 백엔드용")
    public RedirectView naverLogin() {
        return new RedirectView("/oauth2/authorization/naver");
    }
}
