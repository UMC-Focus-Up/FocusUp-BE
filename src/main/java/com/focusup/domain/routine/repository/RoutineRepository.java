package com.focusup.domain.routine.repository;

import com.focusup.entity.Routine;
import com.focusup.entity.User;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@NonNullApi
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    Optional<Routine> findById(Long id);
}
