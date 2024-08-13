package com.focusup.domain.Item.repository;

import com.focusup.entity.Item;
import com.focusup.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.item.id FROM Order o WHERE o.user.id = :userId")
    List<Long> findItemIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT o.item FROM Order o WHERE o.user.id = :userId")
    List<Item> findItemsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.user.id = :userId AND o.item.name = '부활권'")
    void useResurrection(@Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM `orders` WHERE user_id = :userId AND item_id = 15 LIMIT 1", nativeQuery = true)
    void useSeashell(@Param("userId") Long userId);

    Optional<Order> findItemByUserIdAndItemId(Long user_id, Long item_id);
}
