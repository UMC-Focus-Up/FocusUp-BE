package com.focusup.domain.user.dto;

import com.focusup.entity.enums.SocialType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotNull
    @Enumerated(EnumType.STRING)
    private SocialType socialType;
    @NotNull
    private String token;
}
