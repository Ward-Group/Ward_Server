package com.ward.ward_server.api.webcrawling;

import com.ward.ward_server.api.webcrawling.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
