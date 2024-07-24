package com.focusup.domain.level.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LevelResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewLevelResultDTO {
        Long userId;
        Integer newlevel;
    }
}
