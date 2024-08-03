package com.focusup.domain.alarm.service;

import com.focusup.domain.alarm.dto.AlarmResponse;
import com.focusup.domain.routine.repository.RoutineRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Routine;
import com.focusup.entity.User;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.MemberException;
import com.focusup.global.apiPayload.exception.RoutineException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;

    // 반복 루틴 알림 시, 사용자 알림 옵션 선택
    @Transactional
    public AlarmResponse.AlarmResponseDto postAlarmOption(Long routineId, String oauthId, int option) {
        User user = userRepository.findByOauthId(oauthId).orElseThrow(() -> new RoutineException(ErrorCode.USER_NOT_FOUND));
        Routine routine = routineRepository.findById(routineId).orElseThrow(() -> new RoutineException(ErrorCode.ROUTINE_NOT_FOUND));

        switch (option) {
            // 바로 루틴 실행
            case 0:
                user.addPoint(30);
                break;
            // 루틴 미루기
            case 1:
                // 현재 미루기 7번 이하, 유저의 포인트가 5 이상 남아야 미루기 가능
                if (routine.getDelayCount() < 7 && user.getPoint() >= 5) {
                    user.addPoint(-5);
                    routine.changeDelayCount(1);
                } else if (routine.getDelayCount() >= 7){
                    throw (new RoutineException(ErrorCode.DELAY_COUNT_OVER));
                } else {
                    throw (new MemberException(ErrorCode.INSUFFICIENT_BALANCE));
                }
                break;
            // 루틴 포기
            case 2:
                if (user.getLife() > 0) {
                    user.addLife(-1);
                } else {
                    throw (new MemberException(ErrorCode.INSUFFICIENT_LIFE));
                }
                break;
            default:
                break;
        }

        return AlarmResponse.AlarmResponseDto.builder()
                .life(user.getLife())
                .point(user.getPoint())
                .delayCount(routine.getDelayCount())
                .build();
    }
}
