package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import java.time.LocalDate;

public interface RoutineService {
    // 모든 루틴 리스트 조회 service
    public RoutineResponseDTO.MyPage getMyPage(String oauthId);
    // 루틴 완료 service
    public Long finishRoutine(RoutineRequestDTO.FinishRoutine request, Long routineId, String oauthId);

}
