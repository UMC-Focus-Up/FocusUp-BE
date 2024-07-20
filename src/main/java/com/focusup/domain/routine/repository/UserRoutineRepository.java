package com.focusup.domain.routine.repository;

import com.focusup.entity.UserRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoutineRepository extends JpaRepository<UserRoutine, Long> {
}
