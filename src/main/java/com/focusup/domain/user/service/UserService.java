package com.focusup.domain.user.service;

import com.focusup.domain.level.repository.LevelHistoryRepository;
import com.focusup.domain.routine.repository.UserRoutineRepository;
import com.focusup.domain.user.dto.UserResponse;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.*;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LevelHistoryRepository levelHistoryRepository;
    private final UserRoutineRepository userRoutineRepository;

    public UserResponse.homeInfoDTO getHomeInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 유저 조회 및 예외 처리

        LevelHistory levelHistory = levelHistoryRepository.findByUserId(userId);
        Level level = levelHistory.getLevel(); // 유저의 레벨 조회

        List<UserRoutine> userRoutines = userRoutineRepository.findByUser(user); // 유저의 루틴 목록 조회

        // 루틴 정보 기본 값 설정
        Long routineId = 0L;
        String routineName = "";
        LocalTime execTime = LocalTime.of(0, 0);

        if (!userRoutines.isEmpty()) { // 생성된 루틴이 존재한다면
            LocalDateTime now = LocalDateTime.now(); // 현재 시간
            Optional<Routine> closestRoutineOpt = Optional.empty();
            LocalTime closestTime = null;

            for (UserRoutine userRoutine : userRoutines) {
                if (userRoutine.getRepeatCycleDay().contains(now.getDayOfWeek())) { // 오늘 해야하는 루틴일 경우
                    LocalTime startTime = userRoutine.getStartTime();
                    LocalTime goalTime = userRoutine.getGoalTime();
                    LocalTime currentTime = now.toLocalTime();

                    // 현재 또는 미래에 진행해야 하는 경우
                    if ((currentTime.isAfter(startTime) && currentTime.isBefore(goalTime)) || currentTime.isBefore(startTime)) {
                        if (closestTime == null || closestTime.isAfter(startTime)) { // 가장 가까운 루틴 확인
                            closestTime = startTime;
                            closestRoutineOpt = userRoutine.getRoutines().stream()
                                    .filter(routine -> routine.getDate().equals(now.toLocalDate()))
                                    .findFirst();
                            if (closestRoutineOpt.isPresent()) {
                                routineName = userRoutine.getName(); // 루틴 이름 설정
                            }
                        }
                    }
                }
            }

            // 현재 시간에 가장 가까운 루틴 정보 설정
            if (closestRoutineOpt.isPresent()) {
                Routine closestRoutine = closestRoutineOpt.get();
                routineId = closestRoutine.getId();
                execTime = closestRoutine.getExecTime();
            }
        }

        return UserResponse.homeInfoDTO.builder()
                .life(user.getLife())
                .point(user.getPoint())
                .level(level.getLevel())
                .routineId(routineId)
                .routineName(routineName)
                .execTime(execTime)
                .build();
    }

    public UserResponse.characterPageInfoDTO getCharacterPageInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 유저 조회 및 예외 처리


        Item item = user.getCurItem(); // 아이템 조회
        UserResponse.currentItemDTO currentItemDTO = null;

        if(item != null) {
            currentItemDTO = UserResponse.currentItemDTO.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .type(item.getType())
                    .imageUrl(item.getImageUrl())
                    .build();
        }

        return UserResponse.characterPageInfoDTO.builder()
                .life(user.getLife())
                .point(user.getPoint())
                .item(currentItemDTO)
                .build();
    }
}
