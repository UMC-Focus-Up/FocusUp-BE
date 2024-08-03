package com.focusup.domain.Item.repository;

import com.focusup.entity.Item;
import com.focusup.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.item.id FROM Order o WHERE o.user.id = :userId")
    List<Long> findItemIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT o.item FROM Order o WHERE o.user.id = :userId")
    List<Item> findItemsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
