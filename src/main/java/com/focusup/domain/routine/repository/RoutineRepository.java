package com.focusup.domain.routine.repository;

import com.focusup.entity.Routine;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NonNullApi
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findById(Long id);

    @Modifying
    @Query("DELETE FROM Routine r WHERE r.userRoutine.id IN (SELECT ur.id FROM UserRoutine ur WHERE ur.user.id = :userId)")
    void deleteRoutinesByUserId(@Param("userId") Long userId);
}
