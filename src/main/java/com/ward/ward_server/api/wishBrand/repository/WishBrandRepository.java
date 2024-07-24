package com.ward.ward_server.api.wishBrand.repository;

import com.ward.ward_server.api.wishBrand.WishBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishBrandRepository extends JpaRepository<WishBrand, Long>, WishBrandQueryRepository {
    List<WishBrand> findByUserId(Long userId);
    boolean existsByUserIdAndBrandId(long userId, long brandId);

    void deleteByUserIdAndBrandId(long userId, long brandId);
}
