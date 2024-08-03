package com.focusup.domain.user.service;

import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.User;
import com.focusup.global.apiPayload.exception.CustomException;
import com.focusup.global.apiPayload.exception.TokenException;
import com.focusup.global.security.jwt.JwtTokenUtils;
import com.focusup.global.security.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.focusup.global.apiPayload.code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Transactional
    public TokenInfo refreshAccessToken(String refreshToken) {
        // refresh token 이용하여 access token 재발급
        if (!jwtTokenUtils.validateToken(refreshToken)) {
            throw new TokenException(INVALID_TOKEN);
        }
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        TokenInfo newToken = jwtTokenUtils.generateToken(authentication);

        user.setRefreshToken(newToken.getRefreshToken());
        return newToken;
    }

}
