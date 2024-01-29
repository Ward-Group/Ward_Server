package com.ward.ward_server.webcrawling;

import com.ward.ward_server.webcrawling.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

}
