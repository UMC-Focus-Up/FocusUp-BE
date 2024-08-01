package com.focusup.domain.routine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RoutineResponseDTO {

    // 마이페이지 조회
    @Getter
    @Builder
    public static class MyPage {
        private List<UserRoutineResponseDTO.UserRoutine> userRoutines;
        private int level;
        private int successCount;
        private List<DateRoutines> routines;
    }

    // 목표 루틴 리스트 조회 response DTO
    @Getter
    @Builder
    public static class DateRoutines {
        private LocalDate date;
        private double totalAchieveRate;
        private List<Routine> routines;
    }

    // 루틴 상세 정보 DTO
    @Getter
    @Builder
    public static class Routine {
        private Long id;
        private String name;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime targetTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
        private LocalTime execTime ;

        private double achieveRate ;
    }
}
