package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.query.ItemQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemQueryRepository {
    boolean existsByCodeAndBrandId(String code, long brandId);
}
