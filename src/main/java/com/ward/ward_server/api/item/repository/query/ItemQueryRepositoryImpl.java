package com.ward.ward_server.api.item.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.global.Object.enums.HomeSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;
import static com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo.releaseInfo;
import static com.ward.ward_server.api.wishItem.QWishItem.wishItem;
import static com.ward.ward_server.global.Object.Constants.HOME_PAGE_SIZE;

@RequiredArgsConstructor
@Slf4j
public class ItemQueryRepositoryImpl implements ItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ItemSimpleResponse> getHomeSortList(Long userId, LocalDateTime now, Category category, HomeSort homeSort) {
        Set<Long> wishItemIds = wishItemIdListByUser(userId);
        List<Tuple> tuples = queryFactory.select(
                        item.id,
                        getField(homeSort)
                ).from(releaseInfo)
                .leftJoin(releaseInfo.item, item)
                .where(getSortCondition(homeSort, now, wishItemIds), getCategoryCondition(category))
                .fetch();

        log.debug("tuples 검사 {}", tuples);

        Map<Long, LocalDateTime> itemIdAndDateMap = tuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(0, Long.class),
                        Collectors.collectingAndThen(
                                Collectors.mapping(tuple -> tuple.get(1, LocalDateTime.class), Collectors.toList()),
                                dateList -> dateList.stream().min(Comparator.naturalOrder()).orElse(null))
                ));

        log.debug("map 검사 {}", itemIdAndDateMap.size());

        List<Long> top10 = itemIdAndDateMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(HOME_PAGE_SIZE)
                .map(Map.Entry::getKey)
                .toList();

        List<Tuple> result = top10.stream()
                .map(e -> queryFactory.select(
                                item.id,
                                item.koreanName,
                                item.englishName,
                                item.code,
                                item.mainImage,
                                brand.id,
                                brand.koreanName,
                                brand.englishName
                        ).from(item)
                        .leftJoin(item.brand, brand)
                        .where(item.id.eq(e))
                        .fetchOne())
                .toList();

        return itemSimpleResponseList(result, wishItemIds);
    }

    public Page<ItemSimpleResponse> getHomeSortPage(Long userId, LocalDateTime now, Category category, HomeSort homeSort, Pageable pageable) {
        Set<Long> wishItemIds = wishItemIdListByUser(userId);
        List<Tuple> tuples = queryFactory.select(
                        item.id,
                        getField(homeSort)
                ).from(releaseInfo)
                .leftJoin(releaseInfo.item, item)
                .where(getSortCondition(homeSort, now, wishItemIds), getCategoryCondition(category))
                .fetch();

        Map<Long, LocalDateTime> itemIdAndDateMap = tuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(0, Long.class),
                        Collectors.collectingAndThen(
                                Collectors.mapping(tuple -> tuple.get(1, LocalDateTime.class), Collectors.toList()),
                                dateList -> dateList.stream().min(Comparator.naturalOrder()).orElse(null))
                ));

        List<Long> sortedIds = itemIdAndDateMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        List<Tuple> result = pageIds(sortedIds, pageable.getPageNumber(), pageable.getPageSize())
                .stream()
                .map(e -> queryFactory.select(
                                item.id,
                                item.koreanName,
                                item.englishName,
                                item.code,
                                item.mainImage,
                                brand.id,
                                brand.koreanName,
                                brand.englishName
                        ).from(item)
                        .leftJoin(item.brand, brand)
                        .where(item.id.eq(e))
                        .fetchOne())
                .toList();
        return new PageImpl<>(itemSimpleResponseList(result, wishItemIds), pageable, sortedIds.size());
    }

    private List<Long> pageIds(List<Long> itemList, int pageNumber, int pageSize) {
        int fromIndex = pageNumber * pageSize; // 시작 페이지
        int toIndex = Math.min(fromIndex + pageSize, itemList.size()); // 끝 페이지 검사(다음 페이지 or 끝 요소)
        if (fromIndex > toIndex) return new ArrayList<>();
        return itemList.subList(fromIndex, toIndex);
    }

    private BooleanBuilder getSortCondition(HomeSort homeSort, LocalDateTime now, Set<Long> wishItemIds) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        BooleanBuilder builder = new BooleanBuilder();

        switch (homeSort) {
            case DUE_TODAY -> {
                //마감일 = 오늘, 발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
                builder.and(isSameDay(now, releaseInfo.dueDate))
                        .and(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate));
            }
            case RELEASE_NOW -> {
                //발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
                builder.and(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate));
            }
            case RELEASE_WISH -> {
                //발매일 < 지금 < 마감일, 사용자의 관심 상품, 정렬은 마감일 오름차순
                builder.and(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
                        .and(item.id.in(wishItemIds));
            }
            case RELEASE_CONFIRM -> {
                //지금 < 발매일, 정렬은 발매일 오름차순
                builder.and(nowDateTime.before(releaseInfo.releaseDate));
            }
            default -> {
                //생성일 = 지금, 정렬은 생성일 오름차순
                builder.and(isSameDay(now, releaseInfo.createdAt));
            }
        }
        return builder;
    }

    private BooleanExpression getCategoryCondition(Category category) {
        return category == Category.ALL ? null : item.category.eq(category);
    }

    private DateTimePath<java.time.LocalDateTime> getField(HomeSort homeSort) {
        return switch (homeSort) {
            case REGISTER_TODAY -> releaseInfo.createdAt;
            case RELEASE_CONFIRM -> releaseInfo.releaseDate;
            default -> releaseInfo.dueDate;
        };
    }

    private BooleanExpression isSameDay(LocalDateTime dateTime, DateTimePath<LocalDateTime> datePath) {
        log.debug("datePath : {}", datePath);
        return Expressions.numberTemplate(Integer.class, "YEAR({0})", datePath).eq(dateTime.getYear())
                .and(Expressions.numberTemplate(Integer.class, "MONTH({0})", datePath).eq(dateTime.getMonthValue()))
                .and(Expressions.numberTemplate(Integer.class, "DAY({0})", datePath).eq(dateTime.getDayOfMonth()));
    }

    private Set<Long> wishItemIdListByUser(Long userId) {
        if (userId == null) {
            return new HashSet<>();
        }
        return new HashSet<>(
                queryFactory.select(wishItem.item.id)
                        .from(wishItem)
                        .where(wishItem.user.id.eq(userId)).fetch());
    }

    private List<ItemSimpleResponse> itemSimpleResponseList(List<Tuple> tuples, Set<Long> wishItemIds) {
        return tuples.stream()
                .map(e -> new ItemSimpleResponse(
                        e.get(item.id),
                        e.get(item.koreanName),
                        e.get(item.englishName),
                        e.get(item.code),
                        e.get(item.mainImage),
                        e.get(brand.id),
                        e.get(brand.koreanName),
                        e.get(brand.englishName),
                        wishItemIds.contains(e.get(item.id))))
                .toList();
    }
}
