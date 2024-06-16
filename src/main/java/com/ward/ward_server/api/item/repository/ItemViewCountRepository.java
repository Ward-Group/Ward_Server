package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.ItemViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemViewCountRepository extends JpaRepository<ItemViewCount, Long> {
}
