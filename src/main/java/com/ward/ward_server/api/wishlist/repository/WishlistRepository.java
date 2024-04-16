package com.ward.ward_server.api.wishlist.repository;

import com.ward.ward_server.api.wishlist.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    @Query("SELECT wl FROM Wishlist wl JOIN FETCH wl.user JOIN FETCH wl.item WHERE wl.user.id = :userId")
    List<Wishlist> findAllByUserIdWithFetch(@Param("userId") Long userId);

    // userId와 itemId 기반으로 해당 관심 목록이 존재하는지 확인하는 메서드
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserIdAndItemId(long userId, long itemId);
}
