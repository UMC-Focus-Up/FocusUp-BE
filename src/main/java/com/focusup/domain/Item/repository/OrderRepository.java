package com.focusup.domain.Item.repository;

import com.focusup.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.user.id = :userId AND o.item.id = :itemId")
    boolean isPurchasedByUser(@Param("userId") Long userId, @Param("itemId") Long itemId);
}
