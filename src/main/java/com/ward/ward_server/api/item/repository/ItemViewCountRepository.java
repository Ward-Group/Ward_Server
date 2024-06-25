package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemViewCount;
import com.ward.ward_server.api.item.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemViewCountRepository extends JpaRepository<ItemViewCount, Long> {
    Optional<ItemViewCount> findByItemAndCategory(Item item, Category category);
}
