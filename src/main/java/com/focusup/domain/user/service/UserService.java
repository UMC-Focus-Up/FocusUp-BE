package com.focusup.domain.user.service;

import com.focusup.domain.user.dto.UserResponse;
import com.focusup.global.security.jwt.TokenInfo;

public interface UserService {
    public TokenInfo refreshAccessToken(String refreshToken);
    public UserResponse.homeInfoDTO getHomeInfo(Long userId);
    public UserResponse.characterPageInfoDTO getCharacterPageInfo(Long userId);
}
