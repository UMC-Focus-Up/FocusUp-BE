package com.focusup.domain.user.service;

import com.focusup.domain.user.dto.LoginRequest;
import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.global.security.jwt.TokenInfo;
import com.focusup.domain.user.dto.UserResponse;

public interface UserService {
    TokenInfo refreshAccessToken(String refreshToken);
    UserResponse.homeInfoDTO getHomeInfo(String oauthId);
    UserResponse.characterPageInfoDTO getCharacterPageInfo(String oauthId);
    LoginResponse socialLogin(LoginRequest request);

    void withdraw(String oauthId);

}