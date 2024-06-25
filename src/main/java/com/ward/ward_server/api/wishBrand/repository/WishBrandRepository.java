package com.ward.ward_server.api.wishBrand.repository;

import com.ward.ward_server.api.wishBrand.WishBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishBrandRepository extends JpaRepository<WishBrand, Long>, WishBrandQueryRepository {
    boolean existsByUserIdAndBrandId(long userId, long brandId);

    void deleteByUserIdAndBrandId(long userId, long brandId);
}
