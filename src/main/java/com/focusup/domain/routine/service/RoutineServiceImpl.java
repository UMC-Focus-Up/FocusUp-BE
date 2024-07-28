package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.RoutineRequestDTO;
import com.focusup.domain.routine.dto.RoutineResponseDTO;
import com.focusup.domain.routine.repository.RoutineRepository;
import com.focusup.domain.routine.repository.UserRoutineRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Routine;
import com.focusup.entity.User;
import com.focusup.entity.UserRoutine;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.RoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService{
    private final RoutineRepository routineRepository;
    private final UserRoutineRepository userRoutineRepository;
    private final UserRepository userRepository;

    // 유저 루틴 생성 service
    public Long createUserRoutine(RoutineRequestDTO.CreateRoutine request, Long userId) {
        // 종료일 설정(우선 한달
        LocalDate endDate = request.getStartDate().plusMonths(1);
        // List를 EnumSet으로 변경
        EnumSet<DayOfWeek> repeatCyccleDays = EnumSet.copyOf(request.getRepeatCycleDay());
        // 유저 확인
        User user = userRepository.findById(userId).orElseThrow(() -> new RoutineException(ErrorCode.USER_NOT_FOUND));

        // user routine 먼저 생성
        UserRoutine userRoutine = UserRoutine.builder()
                .user(user)
                .name(request.getRoutineName())
                .startDate(request.getStartDate())
                .build();

        // userRoutine 저장
        userRoutine = userRoutineRepository.save(userRoutine);

        // 루틴 생성을 위한 날짜 계산
        List<LocalDate> routineDates = Stream.iterate(request.getStartDate(), date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(request.getStartDate(), endDate))
                .filter(date -> repeatCyccleDays.contains(date.getDayOfWeek()))
                .toList();

        List<Routine> routines = new ArrayList<>();

        // date에 따라 routine 추가
        for (LocalDate date : routineDates) {
            LocalDateTime startDateTime = LocalDateTime.of(date, request.getStartTime());
            LocalDateTime endDateTime = LocalDateTime.of(date, request.getEndTime());

            // 시작 시간과 종료 시간 비교하여 종료 시간이 시작 시간보다 이전인 경우 하루를 추가
            if (request.getEndTime().isBefore(request.getStartTime())) {
                endDateTime = endDateTime.plusDays(1);
            }

            routines.add(createRoutineInfo(startDateTime, endDateTime, userRoutine));
        }

        userRoutine = userRoutine.toBuilder()
                .routines(routines)
                .build();

        return userRoutine.getId();
    }

    @Transactional
    public Routine createRoutineInfo(LocalDateTime startTime, LocalDateTime endTime, UserRoutine userRoutine) {
        // 루틴 생성
        Routine routine = Routine.builder()
                .startTime(startTime)
                .goalTime(endTime)
                .userRoutine(userRoutine)
                .build();

        return routineRepository.save(routine);
    }

    // 모든 루틴 리스트 조회 service
    public RoutineResponseDTO.GetAllRoutineList getAllRoutineList() {
        // 모든 루틴 조회
        List<Routine> routines = routineRepository.findAll();

        // 엔티티 리스트를 DTO 리스트로 변환
        List<RoutineResponseDTO.Routine> routineDTOs = routines.stream()
                .map(routine -> {
                    // startTime의 date만 추출
                    LocalDate startDate = routine.getStartTime().toLocalDate();
                    // goalTime과 startTime 사이의 Duration 계산
                    Duration duration = Duration.between(routine.getStartTime(), routine.getGoalTime());
                    // Duration에서 시간과 분을 추출하여 LocalTime 생성, 초는 00으로 설정
                    LocalTime targetTime = LocalTime.of(
                            (int) duration.toHours(),        // 시간
                            (int) (duration.toMinutes() % 60), // 분
                            0);

                    return RoutineResponseDTO.Routine.builder()
                            .id(routine.getId())
                            .name(routine.getUserRoutine().getName()) // UserRoutine에서 name 가져오기
                            .date(startDate)
                            .targetTime(targetTime)
                            .execTime(routine.getExecTime())
                            .achieveRate((float) routine.getAchieveRate())
                            .build();
                })
                .collect(Collectors.toList());

        // DTO 객체로 변환
        return RoutineResponseDTO.GetAllRoutineList.builder().routines(routineDTOs).build();
    }

    // 특정 일의 루틴 리스트 조회 service
    public RoutineResponseDTO.GetTodayRoutineList getTodayRoutineList(LocalDate date) {
        return null;
    }

    // 루틴 완료 service
    public Long finishRoutine(RoutineRequestDTO.FinishRoutine request, Long routineId) {
        // 루틴 id로 해당 루틴 가져오기
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));
        // 전체 루틴 시간 (분 단위)
        long totalTime = Duration.between(routine.getStartTime(), routine.getGoalTime()).toMinutes();
        // 실행 시간 (분 단위)
        long execTime = Duration.between(LocalTime.MIDNIGHT, request.getExecTime()).toMinutes();
        // 달성률 계산
        double achieveRate = (double) execTime / (totalTime * 100.0);
        // Routine 업데이트
        routine = routine.toBuilder()
                .execTime(request.getExecTime())
                .achieveRate(achieveRate)
                .build();
        // 업데이트된 Routine 저장
        routineRepository.save(routine);

        return routineId;
    }

    // 루틴 삭제 service
    public void deleteRoutine(Long userRoutineId) {
        // userRoutine 찾고 삭제
        userRoutineRepository.findById(userRoutineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));
        userRoutineRepository.deleteById(userRoutineId);
    }
}
