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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
