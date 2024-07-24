package com.focusup.domain.routine.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RoutineRequestDTO {

    // 루틴 설정 request용 DTO
    @Builder
    @Getter
    public static class CreateRoutine {
        private String routineName;
        private String repeatCycleDay;

        private LocalDate date;

        private LocalDateTime StartTime;

        private LocalDateTime EndTime;
    }
}
