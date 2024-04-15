package com.ward.ward_server.api.wishlist.repository;

import com.ward.ward_server.api.wishlist.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    // userId와 itemId 기반으로 해당 관심 목록이 존재하는지 확인하는 메서드
    boolean existsByUserIdAndItemId(Long userId, Long itemId);
}
