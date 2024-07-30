package com.focusup.domain.routine.repository;

import com.focusup.entity.Routine;
import com.focusup.entity.User;
import com.focusup.entity.UserRoutine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoutineRepository extends JpaRepository<UserRoutine, Long> {
    List<UserRoutine> findByUser(User user);
    UserRoutine findByRoutine(Routine routine);
    Optional<UserRoutine> findById(Long id);
}
