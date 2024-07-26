package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.entity.Routine;
import com.focusup.entity.UserRoutine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface RoutineService {
    public Long createUserRoutine(RoutineRequestDTO.CreateRoutine request, Long userId);

    public Routine createRoutineInfo(LocalDateTime startTime, LocalDateTime endTime, UserRoutine userRoutine);

    // 모든 루틴 리스트 조회 service
    public RoutineResponseDTO.GetAllRoutineList getAllRoutineList();

    // 특정 일의 루틴 리스트 조회 service
    public RoutineResponseDTO.GetTodayRoutineList getTodayRoutineList(LocalDate date);

    // 루틴 완료 service
    public Long finishRoutine(Long routineId);

    // 루틴 삭제 service
    public Long deleteRoutine(Long routineId);
}
