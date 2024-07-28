package com.focusup.domain.routine.repository;

import com.focusup.entity.Routine;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@NonNullApi
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findById(Long id);

    // LocalDateTime startDate에서 LocalDate가 같은 것만 호출
    @Query("SELECT r FROM Routine r WHERE DATE(r.startTime) = :date")
    List<Routine> findByStartDate(@Param("date") LocalDate date);
}
