package com.ward.ward_server.api.wishItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishItemRepository extends JpaRepository<WishItem, Long> {
    Page<WishItem> findAllByUserId(long userId, Pageable pageable);

    boolean existsByUserIdAndItemId(long userId, long itemId);

    void deleteByUserIdAndItemId(long userId, long itemId);

    Long countAllByItemId(long itemId);
}
