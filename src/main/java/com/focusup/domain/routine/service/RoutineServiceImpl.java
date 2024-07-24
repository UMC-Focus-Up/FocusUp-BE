package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.domain.routine.repository.RoutineRepository;
import com.focusup.domain.routine.repository.UserRoutineRepository;
import com.focusup.entity.enums.Day;
import com.focusup.global.apiPayload.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService{
    private final RoutineRepository routineRepository;
    private final UserRoutineRepository userRoutineRepository;

    // 루틴 생성 service
    public String createRoutine(String name, LocalDate date, List<Day> days, LocalTime startTiem, LocalTime endTime) {
        return null;
    }

    // 모든 루틴 리스트 조회 service
    public RoutineResponseDTO.GetAllRoutineList getAllRoutineList() {
        return null;
    }

    // 특정 일의 루틴 리스트 조회 service
    public RoutineResponseDTO.GetTodayRoutineList getTodayRoutineList(LocalDate date) {
        return null;
    }

    // 루틴 완료 service
    public Long finishRoutine(Long routineId) {
        return null;
    }

    // 루틴 삭제 service
    public Long deleteRoutine(Long routineId) {
        return null;
    }
}
