package com.ward.ward_server.api.wishItem.repository;

import com.ward.ward_server.api.wishItem.WishItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishItemRepository extends JpaRepository<WishItem, Long>, WishItemQueryRepository {
    List<WishItem> findByUserId(Long userId);
    Page<WishItem> findAllByUserId(long userId, Pageable pageable);

    boolean existsByUserIdAndItemId(long userId, long itemId);

    void deleteByUserIdAndItemId(long userId, long itemId);
}
