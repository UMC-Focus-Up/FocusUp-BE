package com.focusup.domain.level.converter;

import com.focusup.domain.level.dto.LevelResponse;
import com.focusup.entity.LevelHistory;

public class LevelHistoryConverter {
    public static LevelResponse.NewLevelResultDTO toUpdateLevelResultDTO(LevelHistory levelHistory) {
        return LevelResponse.NewLevelResultDTO.builder()
                .userId(levelHistory.getUser().getId())
                .level(levelHistory.getNewLevel().getLevel())
                .isUserLevel(false)
                .build();
    }

    public static LevelResponse.NewLevelResultDTO toLevelResultDTO(LevelHistory levelHistory) {
        return LevelResponse.NewLevelResultDTO.builder()
                .userId(levelHistory.getUser().getId())
                .level(levelHistory.getLevel().getLevel())
                .isUserLevel(true)
                .build();
    }
}
