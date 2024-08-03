package com.focusup.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

public class UserResponse {

    @Builder
    @Getter
    public static class homeInfoDTO {
        int life;
        int point;
        int level;
        Long routineId;
        String routineName;
        LocalTime execTime;
    }

    @Builder
    @Getter
    public static class characterPageInfoDTO {
        int life;
        int point;
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
