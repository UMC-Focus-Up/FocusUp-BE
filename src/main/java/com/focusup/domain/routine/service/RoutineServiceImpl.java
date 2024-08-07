package com.focusup.domain.routine.service;

import com.focusup.domain.level.repository.LevelHistoryRepository;
import com.focusup.domain.level.repository.LevelRepository;
import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.domain.routine.dto.UserRoutineResponseDTO;
import com.focusup.domain.routine.repository.RoutineRepository;
import com.focusup.domain.routine.repository.UserRoutineRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.*;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import com.focusup.global.apiPayload.exception.LevelException;
import com.focusup.global.apiPayload.exception.RoutineException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService{
    private final RoutineRepository routineRepository;
    private final UserRoutineRepository userRoutineRepository;
    private final LevelHistoryRepository levelHistoryRepository;
    private final LevelRepository levelRepository;
    private final UserRepository userRepository;

    // 마이페이지 조회
    @Transactional
    public RoutineResponseDTO.MyPage getMyPage(String oauthId) {
        // 유저 확인
        User user = userRepository.findByOauthId(oauthId).orElseThrow(() -> new RoutineException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "startDate"));
        List<UserRoutine> userRoutines;

        userRoutines = userRoutineRepository.findAll(pageable).getContent();
        userRoutines.forEach(ur -> Hibernate.initialize(ur.getRoutines())); // 컬렉션 초기화

        // 유저 루틴 DTO로 변환
        List<UserRoutineResponseDTO.UserRoutine> userRoutineDTOs = userRoutines.stream()
                .map(ur -> UserRoutineResponseDTO.UserRoutine.builder()
                        .id(ur.getId())
                        .name(ur.getName())
                        .build())
                .collect(Collectors.toList());
        // 루틴이 없는 경우
        List<Routine> routines;
        routines = routineRepository.findAll();

        List<RoutineResponseDTO.DateRoutines> dateRoutineDTOs = routines.stream()
                .collect(Collectors.groupingBy(Routine::getDate))
                .entrySet().stream()
                .map(entry -> convertToDateRoutinesDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        LevelHistory levelHistory = levelHistoryRepository.findByUser(user);
        Hibernate.initialize(levelHistory.getLevel()); // 프록시 초기화

        return RoutineResponseDTO.MyPage.builder()
                .userRoutines(userRoutineDTOs)
                .level(levelHistory.getLevel().getLevel()) // levelHistory를 userId로 조회
                .successCount(levelHistoryRepository.findByUser(user).getSuccessCount()) // levelHistory를 userId로 조회
                .routines(dateRoutineDTOs)
                .build();
    }

    // routineList를 DateRoutinesDTO로 변환
    @Transactional
    public RoutineResponseDTO.DateRoutines convertToDateRoutinesDTO(LocalDate date, List<Routine> routines) {
        double totalAchieveRate = routines.stream()
                .mapToDouble(Routine::getAchieveRate)
                .average()
                .orElse(0.0);

        List<RoutineResponseDTO.Routine> routineDTOs = routines.stream()
                .map(routine -> RoutineResponseDTO.Routine.builder()
                        .id(routine.getId())
                        .name(routine.getUserRoutine().getName()) // Assuming UserRoutine has a name
                        .targetTime(routine.getUserRoutine().getGoalTime()) // Assuming UserRoutine has a targetTime
                        .execTime(routine.getExecTime())
                        .achieveRate(routine.getAchieveRate())
                        .build())
                .collect(Collectors.toList());

        return RoutineResponseDTO.DateRoutines.builder()
                .date(date)
                .totalAchieveRate(totalAchieveRate)
                .routines(routineDTOs)
                .build();
    }

    // 루틴 완료 service
    public Long finishRoutine(RoutineRequestDTO.FinishRoutine request, Long routineId, String oauthId) {
        // 유저 확인
        User user = userRepository.findByOauthId(oauthId).orElseThrow(() -> new RoutineException(ErrorCode.USER_NOT_FOUND));
        // 루틴 id로 해당 루틴 가져오기
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));
        // 전체 루틴 시간 (분 단위)
        long totalTime = Duration.between(LocalTime.MIDNIGHT, routine.getUserRoutine().getGoalTime()).toMinutes();
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

        // 유저의 level successcount 확인
        LevelHistory levelHistory = levelHistoryRepository.findByUser(user);
        levelHistory.addSuccessCount();

        if (levelHistory.getSuccessCount() > 5) {
            long levelUp = levelHistory.getLevel().getLevel() + 1;

            if (levelUp < 8) {
                Level updatedlevel = levelRepository.findById(levelUp).orElseThrow(() -> new LevelException(ErrorCode.LEVEL_NOT_FOUND));
                levelHistory.addLevel(updatedlevel);
                levelHistory.changeSuccessCount(0);
                
            } else {
                throw (new LevelException(ErrorCode.LEVEL_TOO_HIGH));
            }

        }

        return routineId;
    }
}
