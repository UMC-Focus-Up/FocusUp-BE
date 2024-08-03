package com.focusup.domain.level.service;

import com.focusup.domain.level.repository.LevelHistoryRepository;
import com.focusup.domain.level.repository.LevelRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Level;
import com.focusup.entity.LevelHistory;
import com.focusup.entity.User;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.LevelException;
import com.focusup.global.apiPayload.exception.MemberException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {

    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final LevelHistoryRepository levelHistoryRepository;

    @Transactional
    public LevelHistory findLevel (String oauthId) {
        User user = userRepository.findByOauthId(oauthId).orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));
        LevelHistory levelHistory = levelHistoryRepository.findByUser(user);

        return levelHistory;
    }

    @Transactional
    public LevelHistory updateLevel(String oauthId, Long newLevel) {
        LevelHistory levelHistory = findLevel(oauthId);
        Level level = levelRepository.findById(newLevel).orElseThrow(() -> new LevelException(ErrorCode.LEVEL_NOT_FOUND));
        levelHistory.changeLevel(level);

        return levelHistory;
    }
}

