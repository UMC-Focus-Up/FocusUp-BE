package com.focusup.domain.level.service;

import com.focusup.domain.level.repository.LevelHistoryRepository;
import com.focusup.domain.level.repository.LevelRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Level;
import com.focusup.entity.LevelHistory;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.LevelException;
import com.focusup.global.apiPayload.exception.MemberException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {
    @PersistenceContext
    private final EntityManager em;

    private final UserRepository userRepository;
    private final LevelRepository levelRepository;
    private final LevelHistoryRepository levelHistoryRepository;

    @Transactional
    public LevelHistory findLevel (String oauthId) {
        User user = userRepository.findByOauthId(oauthId).orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));
        LevelHistory levelHistory = levelHistoryRepository.findByUserId(user.getId());

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

