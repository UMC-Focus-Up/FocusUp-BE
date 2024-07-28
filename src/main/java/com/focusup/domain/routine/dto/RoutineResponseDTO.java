package com.focusup.domain.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RoutineResponseDTO {

    // 목표 루틴 리스트 조회 response DTO
    @Getter
    @Builder
    public static class GetAllRoutineList {
        private List<Routine> routines;
    }

    // 특정일의 루틴 리스트 조회 response DTO
    @Getter
    @Builder
    public static class GetTodayRoutineList {
        private List<Routine> routines;
    }

    // 루틴 상세 정보 DTO
    @Getter
    @Builder
    public static class Routine {
        private Long id;
        private String name;
        private LocalDate date;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime targetTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime execTime ;

        private float achieveRate ;
    }
}
