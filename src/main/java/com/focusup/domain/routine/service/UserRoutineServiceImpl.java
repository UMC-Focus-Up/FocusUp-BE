package com.focusup.domain.routine.service;

import com.focusup.domain.routine.dto.UserRoutineRequestDTO;
import com.focusup.domain.routine.dto.UserRoutineResponseDTO;
import com.focusup.domain.routine.repository.RoutineRepository;
import com.focusup.domain.routine.repository.UserRoutineRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Routine;
import com.focusup.entity.User;
import com.focusup.entity.UserRoutine;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.RoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
public class UserRoutineServiceImpl implements UserRoutineService{
    private final RoutineRepository routineRepository;
    private final UserRoutineRepository userRoutineRepository;
    private final UserRepository userRepository;

    // 유저 루틴 생성 service
    public Long createUserRoutine(UserRoutineRequestDTO.CreateRoutine request, Long userId) {
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
                .startTime(request.getStartTime())
                .goalTime(request.getEndTime())
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
            routines.add(createRoutineInfo(date, userRoutine));
        }

        userRoutine = userRoutine.toBuilder()
                .routines(routines)
                .build();

        return userRoutine.getId();
    }

    @Transactional
    public Routine createRoutineInfo(LocalDate date, UserRoutine userRoutine) {
        // 루틴 생성
        Routine routine = Routine.builder()
                .date(date)
                .userRoutine(userRoutine)
                .build();

        return routineRepository.save(routine);
    }

    // 유저 루틴 삭제 service
    public void deleteRoutine(Long userRoutineId) {
        // userRoutine 찾고 삭제
        userRoutineRepository.findById(userRoutineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));
        userRoutineRepository.deleteById(userRoutineId);
    }

    // 유저 루틴 전체 리스트 조회 service
    public UserRoutineResponseDTO.GetAllUserRoutineList getAllUserRoutineList() {
        List<UserRoutine> userRoutines = userRoutineRepository.findAll(Sort.by(Sort.Direction.ASC, "startDate"));

        // 유저 루틴 DTO로 변환
        List<UserRoutineResponseDTO.UserRoutine> userRoutineDTOs = userRoutines.stream()
                .map(ur -> UserRoutineResponseDTO.UserRoutine.builder()
                        .id(ur.getId())
                        .name(ur.getName())
                        .build())
                .collect(Collectors.toList());

        // List DTO로 변환
        return UserRoutineResponseDTO.GetAllUserRoutineList.builder().routines(userRoutineDTOs).build();
    }

    // 유저 루틴 상세 정보 조회 service
    public UserRoutineResponseDTO.UserRoutineDetail getUserRoutineDetail(Long userRoutineId) {
        UserRoutine userRoutine = userRoutineRepository.findById(userRoutineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));

        // 유저 루틴 DTO로 변환
        UserRoutineResponseDTO.UserRoutineDetail userRoutineDTOs = UserRoutineResponseDTO.UserRoutineDetail.builder()
                .routineName(userRoutine.getName())
                .repeatCycleDay(userRoutine.getRepeatCycleDay())
                .startTime(userRoutine.getStartTime())
                .endTime(userRoutine.getGoalTime())
                .build();

        // List DTO로 변환
        return userRoutineDTOs;
    }
}
