package com.focusup.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

public class UserResponse {

    @Builder
    @Getter
    public static class homeUserInfoDTO {
        int life;
        int point;
        int level;
        boolean isUserLevel;
    }

    @Builder
    @Getter
    public static class homeRoutineInfoDTO {
        Long routineId;
        String routineName;
        LocalTime execTime;
        LocalTime goalTime;
    }

    @Builder
    @Getter
    public static class characterPageInfoDTO {
        int life;
        int point;
        boolean status;
        UserResponse.currentItemDTO item;
    }

    @Builder
    @Getter
    public static class currentItemDTO {
        Long id;
        String name;
        String type;
        String imageUrl;
    }

}
