package com.focusup.domain.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RoutineRequestDTO {

    // 루틴 설정 request용 DTO
    @Builder
    @Getter
    public static class CreateRoutine {

        private String routineName;
        private List<DayOfWeek> repeatCycleDay;

        private LocalDate startDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime endTime;
    }
}
