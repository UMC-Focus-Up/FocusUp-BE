package com.focusup.domain.user.repository;

import com.focusup.entity.Routine;
import com.focusup.entity.User;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NonNullApi
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findByOauthId(String oauthId);

    Optional<User> findById(Long id);

    @Query("SELECT u.point FROM User u WHERE u.id = :userId")
    Integer getPointById(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :userId")
    void deleteUserById(@Param("userId") Long userId);
}
