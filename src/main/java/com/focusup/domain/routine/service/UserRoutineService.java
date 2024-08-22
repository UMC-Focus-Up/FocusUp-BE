package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.UserRoutineRequestDTO;
import com.focusup.domain.routine.dto.UserRoutineResponseDTO;
import com.focusup.entity.Routine;
import com.focusup.entity.UserRoutine;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserRoutineService {
    public Long createUserRoutine(UserRoutineRequestDTO.CreateRoutine request, String oauthId);

    public Routine createRoutineInfo(LocalDate date, UserRoutine userRoutine);

    // 루틴 삭제 service
    public void deleteRoutine(Long userRoutineId);

    public UserRoutineResponseDTO.UserRoutineDetail getUserRoutineDetail(Long userRoutineId);
}
