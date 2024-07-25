package com.focusup.domain.user.repository;

import com.focusup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u.point FROM User u WHERE u.id = :userId")
    Integer getPointById(@Param("userId") Long userId);
}
