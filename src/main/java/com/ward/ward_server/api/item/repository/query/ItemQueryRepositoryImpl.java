package com.ward.ward_server.api.item.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Tuple> result = queryFactory.select(
                        item.id,
                        item.koreanName,
                        item.englishName,
                        item.code,
                        item.mainImage,
                        brand.id,
                        brand.koreanName,
                        brand.englishName
                ).from(releaseInfo)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(getSortCondition(homeSort, now, wishItemIds), getCategoryCondition(category))
                .orderBy(getSortOrder(homeSort))
                .limit(HOME_PAGE_SIZE)
                .fetch();
        return itemSimpleResponseList(result, wishItemIds);
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

    private OrderSpecifier<LocalDateTime> getSortOrder(HomeSort homeSort) {
        return switch (homeSort) {
            case REGISTER_TODAY -> new OrderSpecifier<>(Order.ASC, releaseInfo.createdAt);
            case RELEASE_CONFIRM -> new OrderSpecifier<>(Order.ASC, releaseInfo.releaseDate);
            default -> new OrderSpecifier<>(Order.ASC, releaseInfo.dueDate);
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
