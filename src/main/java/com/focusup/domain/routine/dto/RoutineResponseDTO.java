package com.focusup.domain.routine.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class RoutineResponseDTO {

    // 목표 루틴 리스트 조회 response DTO
    @Getter
    public static class GetAllRoutineList {
        private List<Routine> routines;
    }

    // 오늘의 루틴 리스트 조회 response DTO
    @Getter
    public static class GetTodayRoutineList {
        private List<Routine> routines;
    }

    // 루틴 상세 정보 DTO
    @Getter
    public static class Routine {
        private Long id;
        private String name;
        private String repeatCycleDay;

        private LocalDateTime TargetTime;

        private LocalDateTime AttainmentTime;

        private float AttainmentRate;
    }
}
