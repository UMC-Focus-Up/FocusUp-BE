package com.focusup.domain.alarm.service;

import com.focusup.domain.alarm.dto.AlarmResponse;

public interface AlarmService {
    // 루틴 알림 시, 사용자의 옵션 선택 service
    public AlarmResponse.AlarmResponseDto postAlarmOption(Long routineId, String oauthId, int option);
}
