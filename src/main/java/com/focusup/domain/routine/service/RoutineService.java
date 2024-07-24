package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.entity.enums.Day;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoutineService {
    public String createRoutine(String name, LocalDate date, List<Day> days, LocalTime startTiem, LocalTime endTime);

    // 모든 루틴 리스트 조회 service
    public RoutineResponseDTO.GetAllRoutineList getAllRoutineList();

    // 특정 일의 루틴 리스트 조회 service
    public RoutineResponseDTO.GetTodayRoutineList getTodayRoutineList(LocalDate date);

    // 루틴 완료 service
    public Long finishRoutine(Long routineId);

    // 루틴 삭제 service
    public Long deleteRoutine(Long routineId);
}
