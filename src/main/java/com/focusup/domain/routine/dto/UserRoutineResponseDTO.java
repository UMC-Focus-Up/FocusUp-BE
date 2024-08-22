package com.focusup.domain.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class UserRoutineResponseDTO {
    // 유저 루틴 리스트 조회 DTO
    @Getter
    @Builder
    public static class GetAllUserRoutineList {
        private List<UserRoutine> routines;
    }

    // 유저 루틴 리스트 및 세부 루틴 조회 DTO
    @Getter
    @Builder
    public static class GetAllUserRoutineSpecRoutineList {
        private List<UserRoutineSpecRoutine> routines;
    }

    // 유저 루틴 제목 DTO
    @Getter
    @Builder
    public static class UserRoutine {
        private Long id;
        private String name;
    }

    // 유저 루틴 제목 및 세부 루틴 DTO
    @Getter
    @Builder
    public static class UserRoutineSpecRoutine {
        private Long id;
        private String name;
        private List<SpecRoutine> specRoutine;
    }

    // 루틴 정보 DTO
    @Getter
    @Builder
    public static class SpecRoutine {
        private Long id;
        private LocalDate date;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime startTime;
    }

    // 유저 루틴 상세 정보 DTO
    @Getter
    @Builder
    public static class UserRoutineDetail {
        private String routineName;
        private List<DayOfWeek> repeatCycleDay;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime endTime;
    }
}
