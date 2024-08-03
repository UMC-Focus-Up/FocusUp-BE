package com.focusup.domain.user.controller;

import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.domain.user.dto.RefreshTokenRequest;
import com.focusup.domain.user.service.UserServiceImpl;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.security.jwt.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import com.focusup.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/auth/success")
    @Operation(summary = "로그인 성공 후 토큰 응답 api, 응답 형태 확인용")
    public Response<LoginResponse> loginSuccess(@Valid LoginResponse loginResponse) {
        return Response.success(loginResponse);
    }

    @PostMapping("/auth/reissue")
    @Operation(summary = "access token 재발급 api")
    public Response<TokenInfo> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return Response.success(userServiceImpl.refreshAccessToken(request.getRefreshToken()));
    }

    @GetMapping("/auth/kakao")
    @Operation(summary = "Kakao Web 소셜 로그인 용 api")
    public RedirectView kakaoLogin() {
        return new RedirectView("/oauth2/authorization/kakao");
    }

    @GetMapping("/auth/naver")
    @Operation(summary = "Naver Web 소셜 로그인 용 api")
    public RedirectView naverLogin() {
        return new RedirectView("/oauth2/authorization/naver");
    }

    @GetMapping("/home")
    @Operation(summary = "홈 조회 api")
    public Response<?> getHomeInfo(@RequestParam Long userId){
        return Response.success(userService.getHomeInfo(userId));
    }

    @GetMapping("/character")
    @Operation(summary = "캐릭터 화면 조회")
    public Response<?> getCharacterPageInfo(@RequestParam Long userId){
        return Response.success(userService.getCharacterPageInfo(userId));
    }
}
