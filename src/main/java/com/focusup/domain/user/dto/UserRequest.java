package com.focusup.domain.user.dto;

import lombok.Getter;

public class UserRequest {

    @Getter
    public static class addPointDTO {
        int point;
    }

    @Getter
    public static class homeRoutineInfoDTO {
        Long routineId;
    }
}
