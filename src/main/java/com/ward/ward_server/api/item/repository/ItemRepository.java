package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByCodeAndDeletedAtIsNull(String code);
    Page<Item> findAllByDeletedAtIsNull(Pageable pageable);
    boolean existsByCode(String code);
}
