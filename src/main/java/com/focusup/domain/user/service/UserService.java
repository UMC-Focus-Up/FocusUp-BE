package com.focusup.domain.user.service;

import com.focusup.domain.user.dto.LoginRequest;
import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.global.security.jwt.TokenInfo;

public interface UserService {
    public TokenInfo refreshAccessToken(String refreshToken);

    public LoginResponse socialLogin(LoginRequest request);
}
