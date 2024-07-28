package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.UserRoutineRequestDTO;
import com.focusup.entity.Routine;
import com.focusup.entity.UserRoutine;

import java.time.LocalDateTime;

public interface UserRoutineService {
    public Long createUserRoutine(UserRoutineRequestDTO.CreateRoutine request, Long userId);

    public Routine createRoutineInfo(LocalDateTime startTime, LocalDateTime endTime, UserRoutine userRoutine);

    // 루틴 삭제 service
    public void deleteRoutine(Long userRoutineId);
}
