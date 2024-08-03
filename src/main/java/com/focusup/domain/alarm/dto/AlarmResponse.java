package com.focusup.domain.alarm.dto;

import lombok.Builder;
import lombok.Getter;

public class AlarmResponse {

    @Builder
    @Getter
    public static class AlarmResponseDto {
        private int life;
        private int point;
        private int delayCount;
    }
}
