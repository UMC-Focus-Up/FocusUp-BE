package com.focusup.domain.user.dto;

import com.focusup.global.security.jwt.TokenInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record LoginResponse(@NotBlank String accessToken, @NotBlank String refreshToken) {
}
