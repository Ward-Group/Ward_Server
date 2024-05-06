package com.ward.ward_server.api.wishItem.repository;

import com.ward.ward_server.api.wishItem.entity.WishItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishItemRepository extends JpaRepository<WishItem, Long> {
    Page<WishItem> findAllByUserId(long userId, Pageable pageable);

    boolean existsByUserIdAndItemId(long userId, long itemId);

    void deleteByUserIdAndItemId(long userId, long itemId);
}
