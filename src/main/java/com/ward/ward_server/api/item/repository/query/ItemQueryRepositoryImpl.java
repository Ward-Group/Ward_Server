package com.ward.ward_server.api.item.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.BrandItemResponse;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.global.Object.enums.BasicSort;
import com.ward.ward_server.global.Object.enums.Section;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;
import static com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo.releaseInfo;
import static com.ward.ward_server.api.wishItem.QWishItem.wishItem;
import static com.ward.ward_server.global.Object.Constants.DATE_YEAR_MONTH_FORMAT;
import static com.ward.ward_server.global.Object.Constants.HOME_PAGE_SIZE;

@RequiredArgsConstructor
@Slf4j
public class ItemQueryRepositoryImpl implements ItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ItemSimpleResponse> getItem10List(Long userId, LocalDateTime now, Category category, Section section) {
        Set<Long> wishItemIds = wishItemIdListByUser(userId);
        List<Tuple> tuples = queryFactory.select(
                        item.id,
                        getField(section)
                ).from(releaseInfo)
                .leftJoin(releaseInfo.item, item)
                .where(getSectionCondition(section, now, null, wishItemIds), getCategoryCondition(category))
                .fetch();

        Map<Long, LocalDateTime> itemIdAndDateMap = tuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(0, Long.class),
                        Collectors.collectingAndThen(
                                Collectors.mapping(tuple -> tuple.get(1, LocalDateTime.class), Collectors.toList()),
                                dateList -> dateList.stream().min(Comparator.naturalOrder()).orElse(null))
                ));

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
                                item.price,
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

    public Page<ItemSimpleResponse> getItemPage(Long userId, LocalDateTime now, Category category, Section section, String yearMonth, Pageable pageable) {
        Set<Long> wishItemIds = wishItemIdListByUser(userId);
        List<Tuple> tuples = queryFactory.select(
                        item.id,
                        getField(section)
                ).from(releaseInfo)
                .leftJoin(releaseInfo.item, item)
                .where(getSectionCondition(section, now, yearMonth, wishItemIds), getCategoryCondition(category))
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
                                item.price,
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

    public Page<BrandItemResponse> getBrandItemPage(long brandId, BasicSort basicSort, Pageable pageable) {
        List<BrandItemResponse> content = queryFactory.select(
                        Projections.constructor(BrandItemResponse.class,
                                item.id,
                                item.mainImage,
                                item.koreanName,
                                item.englishName,
                                item.price)
                ).from(item)
                .leftJoin(item.brand, brand)
                .where(brand.id.eq(brandId))
                .orderBy(getBasicSortOrder(basicSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item)
                .where(item.brand.id.eq(brandId));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier getBasicSortOrder(BasicSort basicSort) {
        return switch (basicSort) {
            case KOREAN_ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, item.koreanName);
            case ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, item.englishName);
            case LATEST -> new OrderSpecifier<>(Order.DESC, item.createdAt);
            default -> new OrderSpecifier<>(Order.DESC, item.viewCount); // VIEW, RANKING
        };
    }

    private List<Long> pageIds(List<Long> itemList, int pageNumber, int pageSize) {
        int fromIndex = pageNumber * pageSize; // 시작 페이지
        int toIndex = Math.min(fromIndex + pageSize, itemList.size()); // 끝 페이지 검사(다음 페이지 or 끝 요소)
        if (fromIndex > toIndex) return new ArrayList<>();
        return itemList.subList(fromIndex, toIndex);
    }

    private BooleanBuilder getSectionCondition(Section section, LocalDateTime now, String yearMonthString, Set<Long> wishItemIds) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        BooleanBuilder builder = new BooleanBuilder();

        switch (section) {
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
            case RELEASE_SCHEDULE -> {
                //지금 < 발매일, 정렬은 발매일 오름차순
                builder.and(nowDateTime.before(releaseInfo.releaseDate));
            }
            case REGISTER_TODAY -> {
                //생성일 = 지금, 정렬은 생성일 오름차순
                builder.and(isSameDay(now, releaseInfo.createdAt));
            }
            default -> {
                //마감일 < 지금, date = 마감날짜 년월
                builder.and(releaseInfo.dueDate.before(nowDateTime))
                        .and(isSameYearAndMonth(YearMonth.parse(yearMonthString, DATE_YEAR_MONTH_FORMAT), releaseInfo.dueDate));
            }
        }
        return builder;
    }

    private BooleanExpression getCategoryCondition(Category category) {
        return category == Category.ALL ? null : item.category.eq(category);
    }

    private DateTimePath<LocalDateTime> getField(Section section) {
        return switch (section) {
            case REGISTER_TODAY -> releaseInfo.createdAt;
            case RELEASE_SCHEDULE -> releaseInfo.releaseDate;
            default -> releaseInfo.dueDate;
        };
    }

    private BooleanExpression isSameDay(LocalDateTime dateTime, DateTimePath<LocalDateTime> datePath) {
        log.debug("datePath : {}", datePath);
        return Expressions.numberTemplate(Integer.class, "YEAR({0})", datePath).eq(dateTime.getYear())
                .and(Expressions.numberTemplate(Integer.class, "MONTH({0})", datePath).eq(dateTime.getMonthValue()))
                .and(Expressions.numberTemplate(Integer.class, "DAY({0})", datePath).eq(dateTime.getDayOfMonth()));
    }

    private BooleanExpression isSameYearAndMonth(YearMonth yearMonth, DateTimePath<LocalDateTime> datePath) {
        log.info("yearMonth : {}", yearMonth);
        return Expressions.numberTemplate(Integer.class, "YEAR({0})", datePath).eq(yearMonth.getYear())
                .and(Expressions.numberTemplate(Integer.class, "MONTH({0})", datePath).eq(yearMonth.getMonthValue()));
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
                        e.get(item.price),
                        e.get(item.mainImage),
                        e.get(brand.id),
                        e.get(brand.koreanName),
                        e.get(brand.englishName),
                        wishItemIds.contains(e.get(item.id))))
                .toList();
    }
}
