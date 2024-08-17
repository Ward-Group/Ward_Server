package com.ward.ward_server.api.item.scheduler;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.ItemTopRank;
import com.ward.ward_server.api.item.entity.ItemViewCount;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.item.repository.ItemTopRankRepository;
import com.ward.ward_server.api.item.repository.ItemViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemViewCountScheduler {

    private static final int ONE_DAY = 1;

    private final ItemViewCountRepository itemViewCountRepository;
    private final ItemTopRankRepository itemTopRankRepository;

    @Scheduled(cron = "0 0 * * * *") // 매 정각마다 실행
    @Transactional
    public void updateViewCounts() {
        executeUpdateViewCounts();
    }

    @Transactional
    public void executeUpdateViewCounts() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusDays(ONE_DAY);

        List<ItemViewCount> viewCounts = itemViewCountRepository.findViewCountsBetween(startTime, now);

        // 카테고리별 아이템 조회수 합계 계산
        Map<Category, Map<Item, Long>> categoryItemViewCounts = viewCounts.stream()
                .collect(Collectors.groupingBy(
                        ItemViewCount::getCategory,
                        Collectors.groupingBy(ItemViewCount::getItem, Collectors.counting())
                ));

        // 기존 ItemTopRank 데이터 삭제
        itemTopRankRepository.deleteAll();

        // 새로운 순위 데이터를 삽입
        categoryItemViewCounts.forEach((category, itemViewCounts) -> {
            int rank = ONE_DAY;
            for (Map.Entry<Item, Long> entry : itemViewCounts.entrySet()) {
                ItemTopRank itemTopRank = ItemTopRank.builder()
                        .item(entry.getKey())
                        .category(category)
                        .itemRank(rank++)
                        .calculatedAt(now)
                        .build();
                itemTopRankRepository.save(itemTopRank);
            }
        });
    }
}

