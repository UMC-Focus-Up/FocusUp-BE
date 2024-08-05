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
    public Long createUserRoutine(UserRoutineRequestDTO.CreateRoutine request, String oauthId) {
        // 종료일 설정(우선 한달
        LocalDate endDate = request.getStartDate().plusMonths(1);
        // List를 EnumSet으로 변경
        EnumSet<DayOfWeek> repeatCycleDays = EnumSet.copyOf(request.getRepeatCycleDay());
        // 유저 확인
        User user = userRepository.findByOauthId(oauthId).orElseThrow(() -> new RoutineException(ErrorCode.USER_NOT_FOUND));

        // user routine 먼저 생성
        UserRoutine userRoutine = UserRoutine.builder()
                .user(user)
                .name(request.getRoutineName())
                .startTime(request.getStartTime())
                .goalTime(request.getEndTime())
                .startDate(request.getStartDate())
                .repeatCycleDay(request.getRepeatCycleDay())
                .build();

        // userRoutine 저장
        userRoutine = userRoutineRepository.save(userRoutine);

        // 루틴 생성을 위한 날짜 계산
        List<LocalDate> routineDates = Stream.iterate(request.getStartDate(), date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(request.getStartDate(), endDate))
                .filter(date -> repeatCycleDays.contains(date.getDayOfWeek()))
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
    public UserRoutineResponseDTO.GetAllUserRoutineSpecRoutineList getAllUserRoutineList() {
        List<UserRoutine> userRoutines = userRoutineRepository.findAll(Sort.by(Sort.Direction.ASC, "startDate"));

        // 유저 루틴 DTO로 변환
        List<UserRoutineResponseDTO.UserRoutineSpecRoutine> userRoutineDTOs = userRoutines.stream()
                .map(ur -> UserRoutineResponseDTO.UserRoutineSpecRoutine.builder()
                        .id(ur.getId())
                        .name(ur.getName())
                        .specRoutine(getSpecRoutine(ur.getId()))
                        .build())
                .collect(Collectors.toList());

        // List DTO로 변환
        return UserRoutineResponseDTO.GetAllUserRoutineSpecRoutineList.builder().routines(userRoutineDTOs).build();
    }

    // 유저 루틴 아이디를 통해 루틴 상제 정보 조회 service
    public List<UserRoutineResponseDTO.SpecRoutine> getSpecRoutine(Long userRoutineId) {
        UserRoutine userRoutine = userRoutineRepository.findById(userRoutineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));
        List<Routine> routines = userRoutine.getRoutines();

        List<UserRoutineResponseDTO.SpecRoutine> specRoutines = routines.stream()
                .map(r -> UserRoutineResponseDTO.SpecRoutine.builder()
                        .id(r.getId())
                        .date(r.getDate())
                        .startTime(userRoutine.getStartTime())
                        .build())
                .collect(Collectors.toList());

        return specRoutines;
    }

    // 유저 루틴 상세 정보 조회 service
    public UserRoutineResponseDTO.UserRoutineDetail getUserRoutineDetail(Long userRoutineId) {
        UserRoutine userRoutine = userRoutineRepository.findById(userRoutineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));

        // List DTO로 변환
        return UserRoutineResponseDTO.UserRoutineDetail.builder()
                .routineName(userRoutine.getName())
                .repeatCycleDay(userRoutine.getRepeatCycleDay())
                .startTime(userRoutine.getStartTime())
                .endTime(userRoutine.getGoalTime())
                .build();
    }
}
