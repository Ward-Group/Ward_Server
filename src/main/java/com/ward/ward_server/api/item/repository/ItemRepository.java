package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
