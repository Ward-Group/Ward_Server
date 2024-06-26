package com.ward.ward_server.api.search.service;

import com.ward.ward_server.api.search.entity.SearchHistory;
import com.ward.ward_server.api.search.repository.SearchHistoryRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ward.ward_server.global.exception.ExceptionCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    public List<SearchHistory> getSearchHistory(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        return searchHistoryRepository.findByUserOrderBySearchDateDesc(user);
    }
}
