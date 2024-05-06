package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByName(String name);
    Optional<Brand> findByName(String name);
    void deleteByName(String name);
    @Query(value = "SELECT * FROM brand ORDER BY (view_count + wish_count) DESC LIMIT 10", nativeQuery = true)
    List<Brand> findBrandListTop10();
}
