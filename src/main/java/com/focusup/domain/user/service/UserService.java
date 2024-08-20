package com.focusup.domain.user.service;

import com.focusup.domain.user.dto.LoginRequest;
import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.global.security.jwt.TokenInfo;
import com.focusup.domain.user.dto.UserResponse;

public interface UserService {
    TokenInfo refreshAccessToken(String refreshToken);
    UserResponse.homeUserInfoDTO getHomeUserInfo(String oauthId);
    UserResponse.homeRoutineInfoDTO getHomeRoutineInfo(String oauthId, Long routineId);
    UserResponse.characterPageInfoDTO getCharacterPageInfo(String oauthId);
    void addPoint(String oauthId, int point);
    LoginResponse socialLogin(LoginRequest request);
    void withdraw(String oauthId);
    void restart(String oauthId);
}