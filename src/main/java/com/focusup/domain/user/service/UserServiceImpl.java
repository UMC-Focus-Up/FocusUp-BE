package com.focusup.domain.user.service;

import com.focusup.domain.Item.repository.OrderRepository;
import com.focusup.domain.level.repository.LevelHistoryRepository;
import com.focusup.domain.level.repository.LevelRepository;
import com.focusup.domain.routine.repository.RoutineRepository;
import com.focusup.domain.user.dto.LoginRequest;
import com.focusup.domain.user.dto.LoginResponse;
import com.focusup.domain.routine.repository.UserRoutineRepository;
import com.focusup.domain.user.dto.UserResponse;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.*;

import com.focusup.entity.enums.Role;
import com.focusup.entity.enums.SocialType;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.CustomException;
import com.focusup.global.apiPayload.exception.TokenException;
import com.focusup.global.apiPayload.exception.UserException;
import com.focusup.global.security.jwt.JwtTokenUtils;
import com.focusup.global.security.jwt.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import static com.focusup.global.apiPayload.code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final LevelHistoryRepository levelHistoryRepository;
    private final LevelRepository levelRepository;
    private final UserRoutineRepository userRoutineRepository;
    private final OrderRepository orderRepository;
    private final RoutineRepository routineRepository;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    @Transactional
    public TokenInfo refreshAccessToken(String refreshToken) {
        // refresh token 이용하여 access token 재발급
        if (!jwtTokenUtils.validateToken(refreshToken)) {
            throw new TokenException(INVALID_TOKEN);
        }
        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        TokenInfo newToken = jwtTokenUtils.generateToken(authentication);

        user.setRefreshToken(newToken.getRefreshToken());
        return newToken;
    }

    @Override
    @Transactional
    public LoginResponse socialLogin(LoginRequest request) {
        SocialType socialType = request.getSocialType();
        String socialId = request.getId();
        String oauthId = socialType + "_" + socialId;

        if (socialType != SocialType.NAVER && socialType != SocialType.KAKAO) {
            throw new UserException(ErrorCode.UNAUTHORIZED);
        }

        User user = getOrSave(oauthId, socialType); // 신규 유저일 경우 회원가입
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getOauthId(), null, Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
        TokenInfo tokenInfo = jwtTokenUtils.generateToken(authentication);
        user.setRefreshToken(tokenInfo.getRefreshToken());

        return new LoginResponse(tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
    }

    @Transactional
    @Override
    public void withdraw(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 관련 엔티티 삭제
        levelHistoryRepository.deleteByUserId(user.getId());
        orderRepository.deleteByUserId(user.getId());
        routineRepository.deleteRoutinesByUserId(user.getId());
        userRoutineRepository.deleteByUserId(user.getId());

        // 사용자 삭제
        userRepository.deleteUserById(user.getId());
    }

    @Transactional
    @Override
    public void restart(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.restart(); // 유저의 생명, 포인트, 장착 아이템 초기화
        orderRepository.deleteByUserId(user.getId()); // 아이템 구매 기록 삭제
    }


    private User getOrSave(String oauthId, SocialType socialType) {
        User user = userRepository.findByOauthId(oauthId).orElse(null);

        if (user == null) { // 새로운 유저
            user = User.builder()
                    .oauthId(oauthId)
                    .socialType(socialType)
                    .role(Role.USER)
                    .build();
            user = userRepository.save(user);

            Level initLevel = levelRepository.findById(Long.valueOf(1))
                    .orElseThrow(() -> new CustomException(ErrorCode.LEVEL_NOT_FOUND));

            LevelHistory initLevelHistory = LevelHistory.builder() // 유저의 levelHistory 초기화
                    .user(user)
                    .level(initLevel)
                    .newLevel(initLevel)
                    .build();

            levelHistoryRepository.save(initLevelHistory);
        }
        return user;
    }

    @Transactional
    @Override
    public UserResponse.homeUserInfoDTO getHomeInfo(String oauthId, Long routineId) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 유저 조회 및 예외 처리

        int level;
        boolean isUserLevel;

        // 유저의 레벨 조회
        if (levelHistoryRepository.findByUser(user).getNewLevel().getLevel() < levelHistoryRepository.findByUser(user).getLevel().getLevel()) {
            level = levelHistoryRepository.findByUser(user).getNewLevel().getLevel();
            isUserLevel = false;
        } else {
            level = levelHistoryRepository.findByUser(user).getLevel().getLevel();
            isUserLevel = true;
        }

        List<UserRoutine> userRoutines = userRoutineRepository.findByUser(user); // 유저의 루틴 목록 조회

        // 루틴 정보 기본 값 설정
        Long selectedRoutineId = 0L;
        String selectedRoutineName = "";
        LocalTime routineExecTime = LocalTime.of(0, 0);
        LocalTime routineGoalTime = LocalTime.of(0, 0);
        LocalDateTime now = LocalDateTime.now(); // 현재 시간

        if(routineId == 0){ // 선택한 루틴이 없을 경우
            if (!userRoutines.isEmpty()) { // 생성된 루틴이 존재한다면
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
                                    selectedRoutineName = userRoutine.getName(); // 루틴 이름 설정
                                }
                            }
                        }
                    }
                }

                // 현재 시간에 가장 가까운 루틴 정보 설정
                if (closestRoutineOpt.isPresent()) {
                    Routine closestRoutine = closestRoutineOpt.get();
                    selectedRoutineId = closestRoutine.getId();
                    selectedRoutineName = closestRoutine.getUserRoutine().getName();
                    routineExecTime = closestRoutine.getExecTime();
                    routineGoalTime = closestRoutine.getUserRoutine().getGoalTime();
                }
            }
        }
        else{ // 선택한 루틴이 있을 경우
            UserRoutine userRoutine = userRoutineRepository.findById(routineId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_ROUTINE_NOT_FOUND));
            Optional<Routine> closestRoutineOpt = userRoutine.getRoutines().stream() // 현재 또는 미래에 해야 할 루틴 중 가장 가까운 루틴
                    .filter(routine -> routine.getDate().isAfter(now.toLocalDate()) || routine.getDate().isEqual(now.toLocalDate()))
                    .findFirst();

            if(closestRoutineOpt.isPresent()){
                Routine closestRoutine = closestRoutineOpt.get();
                selectedRoutineId = closestRoutine.getId();
                selectedRoutineName = userRoutine.getName();
                routineExecTime = closestRoutine.getExecTime();
                routineGoalTime = closestRoutine.getUserRoutine().getGoalTime();
            }
            else{
                throw new CustomException(ErrorCode.ROUTINE_NOT_FOUND);
            }
        }

        return UserResponse.homeUserInfoDTO.builder()
                .life(user.getLife())
                .point(user.getPoint())
                .level(level)
                .isUserLevel(isUserLevel)
                .routineId(selectedRoutineId)
                .routineName(selectedRoutineName)
                .execTime(routineExecTime)
                .goalTime(routineGoalTime)
                .build();
    }

    @Transactional
    @Override
    public UserResponse.characterPageInfoDTO getCharacterPageInfo(String oauthId) {
        User user = userRepository.findByOauthId(oauthId)
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
                .status(user.getLife() > 0)
                .item(currentItemDTO)
                .build();
    }

    @Transactional
    @Override
    public void addPoint(String oauthId, int point) {
        User user = userRepository.findByOauthId(oauthId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)); // 유저 조회 및 예외 처리

        user.addPoint(point); // 포인트 추가
    }

}
