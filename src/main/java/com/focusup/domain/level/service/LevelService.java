package com.focusup.domain.level.service;

import com.focusup.domain.level.converter.LevelHistoryConverter;
import com.focusup.domain.level.repository.LevelRepository;
import com.focusup.domain.user.repository.UserRepository;
import com.focusup.entity.Level;
import com.focusup.entity.LevelHistory;
import com.focusup.entity.User;
import com.focusup.global.apiPayload.code.ErrorCode;
import com.focusup.global.apiPayload.exception.LevelException;
import com.focusup.global.apiPayload.exception.MemberException;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LevelService {
    private final EntityManager em;
    private final UserRepository userRepository;
    private final LevelRepository levelRepository;

    @Transactional
    public LevelHistory findLevel (Long userId) {
        return em.createQuery("select lh from LevelHistory lh where lh.user.id = :userId", LevelHistory.class)
                .getSingleResult();
    }

    @Transactional
    public LevelHistory updateLevel(Long userId, Long newLevel) {
        User user = userRepository.findById(userId).orElseThrow(() -> new MemberException(ErrorCode.USER_NOT_FOUND));
        Level level = levelRepository.findById(newLevel).orElseThrow(() -> new LevelException(ErrorCode.LEVEL_NOT_FOUND));

        LevelHistory updateLevel = LevelHistoryConverter.toUpdateLevel(user, level);

        return updateLevel;
    }
}

