package com.focusup.domain.alarm.controller;

import com.focusup.domain.alarm.dto.AlarmResponse;
import com.focusup.domain.alarm.service.AlarmService;
import com.focusup.global.apiPayload.Response;
import com.focusup.global.handler.annotation.Auth;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @PostMapping("/user/{routineId}")
    @Operation(summary ="반복 루틴 알림 API")
    public Response<AlarmResponse.AlarmResponseDto> postAlarmOption (@PathVariable("routineId") Long routineId,
                                                                     @Auth String oauthId,
                                                                     @RequestParam("option") int option) {
        AlarmResponse.AlarmResponseDto alarmResponseDto = alarmService.postAlarmOption(routineId, oauthId, option);
        return Response.success(alarmResponseDto);
    }

    @GetMapping("/user")
    @Operation(summary ="반복 루틴 알림 생명 및 포인트 조회 API")
    public Response<AlarmResponse.AlarmUserInfoDto> getUserInfo (@Auth String oauthId) {
        AlarmResponse.AlarmUserInfoDto alarmUserInfoDto = alarmService.getAlarmUserInfo(oauthId);
        return Response.success(alarmUserInfoDto);
    }
}
