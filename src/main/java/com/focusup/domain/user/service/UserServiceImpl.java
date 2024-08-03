package com.focusup.domain.user.service;

import com.focusup.domain.level.repository.LevelHistoryRepository;
import com.focusup.domain.level.repository.LevelRepository;
import com.focusup.domain.user.dto.LoginRequest;
import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Level;
import com.focusup.entity.LevelHistory;
import com.focusup.entity.User;
import com.focusup.entity.enums.Role;
import com.focusup.entity.enums.SocialType;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import com.focusup.global.apiPayload.exception.TokenException;
import com.focusup.global.apiPayload.exception.UserException;
import com.focusup.global.security.feign.KakaoClient;
import com.focusup.global.security.feign.KakaoUserResponse;
import com.focusup.global.security.feign.NaverClient;
import com.focusup.global.security.feign.NaverUserResponse;
import com.focusup.global.security.jwt.JwtTokenUtils;
import com.focusup.global.security.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.focusup.entity.enums.SocialType.KAKAO;
import static com.focusup.entity.enums.SocialType.NAVER;
import static com.focusup.global.apiPayload.code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final LevelHistoryRepository levelHistoryRepository;
    private final LevelRepository levelRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;

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

    @Override
    @Transactional
    public LoginResponse socialLogin(LoginRequest request) {
        SocialType socialType = request.getSocialType();
        String oauthId;
        if (socialType == NAVER) {
            NaverUserResponse userInfo = naverClient.getUserInfo("Bearer " + request.getToken());
            oauthId = userInfo.getResponse().getId();
        } else if (socialType == KAKAO) {
            KakaoUserResponse userInfo = kakaoClient.getUserInfo("Bearer " + request.getToken());
            oauthId = userInfo.getId();
        } else {
            throw new UserException(UNSUPPORTED_SOCIAL_TYPE);
        }
        if(oauthId == null) throw new UserException(UNAUTHORIZED);

        User user = getOrSave(oauthId, socialType);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getOauthId(), null, Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
        TokenInfo tokenInfo = jwtTokenUtils.generateToken(authentication);
        user.setRefreshToken(tokenInfo.getRefreshToken());

        return new LoginResponse(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
    }


    private User getOrSave(String oauthId, SocialType socialType) {
        User user = userRepository.findByOauthId(oauthId).orElse(null);

        if (user == null) { // 새로운 유저
            user = User.builder()
                    .oauthId(oauthId)
                    .socialType(socialType)
                    .role(Role.USER)
                    .build();
            user = userRepository.save(user);

            Level initLevel = levelRepository.findById(Long.valueOf(1))
                    .orElseThrow(() -> new CustomException(ErrorCode.LEVEL_NOT_FOUND));

            LevelHistory initLevelHistory = LevelHistory.builder() // 유저의 levelHistory 초기화
                    .user(user)
                    .level(initLevel)
                    .newLevel(initLevel)
                    .build();

            levelHistoryRepository.save(initLevelHistory);
        }
        return user;
    }

}
