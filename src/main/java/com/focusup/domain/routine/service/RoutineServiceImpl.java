package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.domain.routine.dto.UserRoutineResponseDTO;
import com.focusup.domain.routine.repository.RoutineRepository;
import com.focusup.domain.routine.repository.UserRoutineRepository;
import com.focusup.entity.Routine;
import com.focusup.entity.UserRoutine;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.RoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService{
    private final RoutineRepository routineRepository;
    private final UserRoutineRepository userRoutineRepository;

    // 마이페이지 조회
    public RoutineResponseDTO.MyPage getMyPage() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "startDate"));
        List<UserRoutine> userRoutines = userRoutineRepository.findAll(pageable).getContent();

        // 유저 루틴 DTO로 변환
        List<UserRoutineResponseDTO.UserRoutine> userRoutineDTOs = userRoutines.stream()
                .map(ur -> UserRoutineResponseDTO.UserRoutine.builder()
                        .id(ur.getId())
                        .name(ur.getName())
                        .build())
                .collect(Collectors.toList());

        List<Routine> routines = routineRepository.findAll();
        List<RoutineResponseDTO.DateRoutines> dateRoutineDTOs = routines.stream()
                .collect(Collectors.groupingBy(Routine::getDate))
                .entrySet().stream()
                .map(entry -> convertToDateRoutinesDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());


        return RoutineResponseDTO.MyPage.builder()
                .userRoutines(userRoutineDTOs)
                .level(1) // levelHistory를 userId로 조회
                .successCount(10) // levelHistory를 userId로 조회
                .routines(dateRoutineDTOs)
                .build();
    }

    // routineList를 DateRoutinesDTO로 변환
    private RoutineResponseDTO.DateRoutines convertToDateRoutinesDTO(LocalDate date, List<Routine> routines) {
        List<RoutineResponseDTO.Routine> routineDTOs = routines.stream()
                .map(this::convertToRoutineDTO)
                .collect(Collectors.toList());
        double totalAchieveRate = routineDTOs.stream()
                .mapToDouble(RoutineResponseDTO.Routine::getAchieveRate)
                .average()
                .orElse(0.0);
        return RoutineResponseDTO.DateRoutines.builder()
                .date(date)
                .totalAchieveRate(totalAchieveRate)
                .routines(routineDTOs)
                .build();
    }

    // routine을 routineDTO로 변환
    private RoutineResponseDTO.Routine convertToRoutineDTO(Routine routine) {
        Duration duration = Duration.between(routine.getUserRoutine().getStartTime(), routine.getUserRoutine().getGoalTime());
        LocalTime targetTime = LocalTime.of(
                (int) duration.toHours(),
                (int) (duration.toMinutes() % 60),
                0); // 초는 0으로 설정

        return RoutineResponseDTO.Routine.builder()
                .id(routine.getId())
                .name(routine.getUserRoutine().getName()) // UserRoutine에서 name 가져오기
                .targetTime(targetTime)
                .execTime(routine.getExecTime())
                .achieveRate(routine.getAchieveRate())
                .build();
    }

    // 루틴 완료 service
    public Long finishRoutine(RoutineRequestDTO.FinishRoutine request, Long routineId) {
        // 루틴 id로 해당 루틴 가져오기
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));
        // 전체 루틴 시간 (분 단위)
        long totalTime = Duration.between(routine.getUserRoutine().getStartTime(), routine.getUserRoutine().getGoalTime()).toMinutes();
        // 실행 시간 (분 단위)
        long execTime = Duration.between(LocalTime.MIDNIGHT, request.getExecTime()).toMinutes();
        // 달성률 계산
        double achieveRate = ((double) execTime / totalTime) * 100.0;
        // Routine 업데이트
        routine = routine.toBuilder()
                .execTime(request.getExecTime())
                .achieveRate(achieveRate)
                .build();
        // 업데이트된 Routine 저장
        routineRepository.save(routine);

        return routineId;
    }

}
