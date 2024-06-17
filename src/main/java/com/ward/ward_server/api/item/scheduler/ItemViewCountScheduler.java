package com.ward.ward_server.api.item.scheduler;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemViewCount;
import com.ward.ward_server.api.item.entity.enumtype.Category;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.item.repository.ItemViewCountRepository;
import com.ward.ward_server.api.item.service.TopItemsCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemViewCountScheduler {

    private final ItemRepository itemRepository;
    private final ItemViewCountRepository itemViewCountRepository;
    private final TopItemsCacheService topItemsCacheService;

    @Scheduled(cron = "0 0 * * * *") // 매 정각마다 실행
    public void updateViewCounts() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(1);

        List<Item> items = itemRepository.findAll();

        // 그룹별 아이템 조회수 집계
        Map<Category, Map<Long, Long>> viewCountMap = items.stream()
                .collect(Collectors.groupingBy(Item::getCategory,
                        Collectors.groupingBy(Item::getId, Collectors.summingLong(Item::getViewCount))));

        // ItemViewCount 테이블 업데이트
        viewCountMap.forEach((category, itemViewCounts) -> {
            itemViewCounts.forEach((itemId, viewCount) -> {
                Item item = itemRepository.findById(itemId).orElseThrow();
                ItemViewCount itemViewCount = ItemViewCount.builder()
                        .category(category)
                        .item(item)
                        .viewCount(viewCount)
                        .calculatedAt(now)
                        .build();
                itemViewCountRepository.save(itemViewCount);
            });
        });

        // 캐시 업데이트
        topItemsCacheService.updateTopItemsCache(viewCountMap);

        log.info("View counts updated at {}", now);
    }
}
