package com.focusup.global.security.oauth;

import com.focusup.domain.level.repository.LevelHistoryRepository;
import com.focusup.domain.level.repository.LevelRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Level;
import com.focusup.entity.LevelHistory;
import com.focusup.entity.User;
import com.focusup.entity.enums.Role;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import com.focusup.global.apiPayload.exception.RoutineException;
import com.focusup.global.apiPayload.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final LevelHistoryRepository levelHistoryRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 유저 정보 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        // Custom Principal name 생성
        String userNameAttributeName = registrationId + "_" + oAuth2UserInfo.id();

        // attributes에 userNameAttributeName 추가
        Map<String, Object> updatedAttributes = new HashMap<>(oAuth2UserAttributes);
        updatedAttributes.put(userNameAttributeName, userNameAttributeName);

        // 회원가입 및 로그인
        User user = getOrSave(oAuth2UserInfo);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                updatedAttributes,
                userNameAttributeName
        );
    }

    private User getOrSave(OAuth2UserInfo oAuth2UserInfo) {
        String oauthId = oAuth2UserInfo.socialType() + "_" +  oAuth2UserInfo.id();
        User user = userRepository.findByOauthId(oauthId).orElse(null);

        if (user == null) { // 새로운 유저
            user = User.builder()
                    .oauthId(oauthId)
                    .socialType(oAuth2UserInfo.socialType())
                    .role(Role.USER)
                    .build();
            user = userRepository.save(user);

            Level initLevel = levelRepository.findById(Long.valueOf(1))
                    .orElseThrow(() -> new CustomException(ErrorCode.LEVEL_NOT_FOUND));

            LevelHistory initLevelHistory = LevelHistory.builder()
                    .user(user)
                    .level(initLevel)
                    .newLevel(initLevel)
                    .build();

            levelHistoryRepository.save(initLevelHistory);
        }
        return user;
    }

    @Transactional
    public void setRefreshToken(Authentication authentication, String refreshToken) {
        String oauthId = authentication.getName();

        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        user.setRefreshToken(refreshToken);
    }

}