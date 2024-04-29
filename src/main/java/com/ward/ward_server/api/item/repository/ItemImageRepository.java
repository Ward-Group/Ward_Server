package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
}
