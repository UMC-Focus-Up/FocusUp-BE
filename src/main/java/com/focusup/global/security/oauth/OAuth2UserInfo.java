package com.focusup.global.security.oauth;

import com.focusup.entity.enums.SocialType;
import com.focusup.global.apiPayload.exception.AuthException;
import lombok.Builder;

import java.util.Map;

import static com.focusup.global.apiPayload.code.ErrorCode.ILLEGAL_REGISTRATION_ID;

@Builder
public record OAuth2UserInfo(
        String id,
        SocialType socialType
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {

        return OAuth2UserInfo.builder()
                .id(String.valueOf(attributes.get("id")))
                .socialType(SocialType.KAKAO)
                .build();
    }

}