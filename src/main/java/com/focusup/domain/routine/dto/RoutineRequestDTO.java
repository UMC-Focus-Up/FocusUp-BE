package com.focusup.domain.routine.dto;

import com.focusup.entity.enums.Day;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RoutineRequestDTO {

    // 루틴 설정 request용 DTO
    @Builder
    @Getter
    public static class CreateRoutine {
        private String routineName;
        private List<Day> repeatCycleDay;

        private LocalDate date;

        private LocalTime StartTime;

        private LocalTime EndTime;
    }
}
