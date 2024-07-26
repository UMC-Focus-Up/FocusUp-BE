package com.focusup.domain.user.repository;

import com.focusup.entity.Routine;
import com.focusup.entity.User;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NonNullApi
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
}
