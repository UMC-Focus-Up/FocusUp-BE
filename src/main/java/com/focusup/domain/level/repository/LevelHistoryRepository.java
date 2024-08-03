package com.focusup.domain.level.repository;

import com.focusup.entity.LevelHistory;
import com.focusup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelHistoryRepository extends JpaRepository<LevelHistory, Long> {
    LevelHistory findByUser(User user);
}
