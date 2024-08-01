package com.focusup.domain.level.repository;

import com.focusup.entity.LevelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LevelHistoryRepository extends JpaRepository<LevelHistory, Long> {
    @Query("select lh from LevelHistory lh where lh.user.id = :userId")
    LevelHistory findByUserId(@Param("userId") Long userId);
}
