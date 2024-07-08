package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.entity.ItemTopRank;
import com.ward.ward_server.api.item.entity.enums.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemTopRankRepository extends JpaRepository<ItemTopRank, Long> {

    @Query("SELECT itr FROM ItemTopRank itr WHERE itr.category = :category ORDER BY itr.itemRank ASC")
    List<ItemTopRank> findTopItemsByCategory(@Param("category") Category category, Pageable pageable);

    @Query("SELECT itr FROM ItemTopRank itr ORDER BY itr.itemRank ASC")
    List<ItemTopRank> findTopItems(Pageable pageable);
}
