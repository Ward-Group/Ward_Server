package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.item.entity.ItemViewCount;
import com.ward.ward_server.api.item.entity.enumtype.Category;
import com.ward.ward_server.api.item.repository.ItemViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TopItemsCacheService {

    private final ItemViewCountRepository itemViewCountRepository;
    private Map<Category, List<ItemViewCount>> topItemsCache = new HashMap<>();

    public void updateTopItemsCache(Map<Category, Map<Long, Long>> viewCountMap) {
        for (Map.Entry<Category, Map<Long, Long>> entry : viewCountMap.entrySet()) {
            Category category = entry.getKey();
            Map<Long, Long> itemViewCounts = entry.getValue();

            List<ItemViewCount> topItems = itemViewCounts.entrySet().stream()
                    .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                    .limit(10)
                    .map(itemEntry -> itemViewCountRepository.findById(itemEntry.getKey()).orElseThrow())
                    .collect(Collectors.toList());

            topItemsCache.put(category, topItems);
        }
    }

    public List<ItemViewCount> getTopItemsByCategory(Category category) {
        return topItemsCache.getOrDefault(category, Collections.emptyList());
    }
}
