package com.ward.ward_server.api.wishBrand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishBrandRepository extends JpaRepository<WishBrand, Long> {
    boolean existsByUserIdAndBrandId(long userId, long brandId);

    Page<WishBrand> findAllByUserId(long userId, Pageable pageable);

    void deleteByUserIdAndBrandId(long userId, long brandId);

    Integer countAllByBrandId(long brandId);
}
