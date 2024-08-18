package com.focusup.domain.level.converter;

import com.focusup.domain.level.dto.LevelResponse;
import com.focusup.entity.LevelHistory;

public class LevelHistoryConverter {
    public static LevelResponse.NewLevelResultDTO toUpdateLevelResultDTO(LevelHistory levelHistory, Long level) {
        return LevelResponse.NewLevelResultDTO.builder()
                .level(levelHistory.getNewLevel().getLevel())
                .isUserLevel(level == 0)
                .build();
    }
}
