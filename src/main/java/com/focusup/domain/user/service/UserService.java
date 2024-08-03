package com.focusup.domain.user.service;

import com.focusup.global.security.jwt.TokenInfo;

public interface UserService {
    public TokenInfo refreshAccessToken(String refreshToken);
}
