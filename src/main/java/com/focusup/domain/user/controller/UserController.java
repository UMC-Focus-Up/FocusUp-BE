package com.focusup.domain.user.controller;

import com.focusup.domain.user.dto.LoginRequest;
import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.domain.user.dto.RefreshTokenRequest;
import com.focusup.domain.user.dto.UserRequest;
import com.focusup.domain.user.service.UserService;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.handler.annotation.Auth;
import com.focusup.global.security.jwt.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/login")
    @Operation(summary = "소셜 로그인 api")
    public Response<LoginResponse> socialLogin(@RequestBody @Valid LoginRequest request){
        return  Response.success(userService.socialLogin(request));
    }

    @PostMapping("/auth/reissue")
    @Operation(summary = "access token 재발급 api")
    public Response<TokenInfo> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return Response.success(userService.refreshAccessToken(request.getRefreshToken()));
    }

    @DeleteMapping("/withdraw")
    @Operation(summary = "회원 탈퇴 api")
    public Response<Void> withdraw(@Auth String oauthId) {
        userService.withdraw(oauthId);
        return Response.success();
    }

    @GetMapping("/home")
    @Operation(summary = "홈 화면 정보 조회 api")
    public Response<?> getHomeUserInfo(@Auth String oauthId, @RequestParam Long routineId){
        return Response.success(userService.getHomeInfo(oauthId, routineId));
    }

    @GetMapping("/character")
    @Operation(summary = "캐릭터 화면 조회")
    public Response<?> getCharacterPageInfo(@Auth String oauthId){
        return Response.success(userService.getCharacterPageInfo(oauthId));
    }

    @PostMapping("/restart")
    @Operation(summary = "게임 오버 시 다시 시작")
    public Response<?> restart(@Auth String oauthId){
        userService.restart(oauthId);
        return Response.success("정상적으로 다시 시작하였습니다");
    }

    @PostMapping("/addPoint")
    @Operation(summary = "포인트(물고기) 추가 api")
    public Response<?> addPoint(@Auth String oauthId, @RequestBody UserRequest.addPointDTO request){
        userService.addPoint(oauthId, request.getPoint());
        return Response.success("포인트를 " + request.getPoint() + " 추가하였습니다");
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
