package com.ward.ward_server.api.search.repository;

import com.ward.ward_server.api.search.entity.SearchHistory;
import com.ward.ward_server.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserOrderBySearchDateDesc(User user);
}
