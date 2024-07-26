package com.focusup.domain.Item.repository;

import com.focusup.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.item.id FROM Order o WHERE o.user.id = :userId")
    List<Long> findItemIdsByUserId(@Param("userId") Long userId);
}
