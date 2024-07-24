package com.focusup.domain.level.converter;

import com.focusup.domain.level.dto.LevelResponse;
import com.focusup.entity.Level;
import com.focusup.entity.LevelHistory;
import com.focusup.entity.User;

public class LevelHistoryConverter {
    public static LevelHistory toUpdateLevel(User user, Level level) {
        return LevelHistory.builder()
                .user(user)
                .newLevel(level)
                .build();
    }

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
