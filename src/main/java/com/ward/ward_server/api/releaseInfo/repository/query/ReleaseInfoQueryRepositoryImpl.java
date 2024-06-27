package com.ward.ward_server.api.releaseInfo.repository.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.global.Object.enums.HomeSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;
import static com.ward.ward_server.api.releaseInfo.entity.QDrawPlatform.drawPlatform;
import static com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo.releaseInfo;
import static com.ward.ward_server.api.wishItem.QWishItem.wishItem;
import static com.ward.ward_server.global.Object.Constants.HOME_PAGE_SIZE;

@RequiredArgsConstructor
@Slf4j
public class ReleaseInfoQueryRepositoryImpl implements ReleaseInfoQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ReleaseInfoSimpleResponse> getHomeSortList(long userId, LocalDateTime now, Category category, HomeSort homeSort) {
        return queryFactory.select(Projections.constructor(ReleaseInfoSimpleResponse.class,
                        releaseInfo.id,
                        drawPlatform.koreanName,
                        drawPlatform.englishName,
                        item.id,
                        item.mainImage,
                        item.koreanName,
                        item.englishName,
                        releaseInfo.releaseMethod,
                        releaseInfo.dueDate))
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(getSortCondition(userId, homeSort, now), getCategoryCondition(category))
                .orderBy(getSortOrder(homeSort))
                .limit(HOME_PAGE_SIZE)
                .fetch();
    }

    public Page<ReleaseInfoSimpleResponse> getHomeSortPage(long userId, LocalDateTime now, Category category, HomeSort homeSort, Pageable pageable) {
        List<ReleaseInfoSimpleResponse> result = queryFactory.select(Projections.constructor(ReleaseInfoSimpleResponse.class,
                        releaseInfo.id,
                        drawPlatform.koreanName,
                        drawPlatform.englishName,
                        item.id,
                        item.mainImage,
                        item.koreanName,
                        item.englishName,
                        releaseInfo.releaseMethod,
                        releaseInfo.dueDate))
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(getSortCondition(userId, homeSort, now), getCategoryCondition(category))
                .orderBy(getSortOrder(homeSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.debug("result size {}", result.size());
        log.debug("\n결과: {}", result.stream()
                .map(e -> e.toString())
                .collect(Collectors.joining("\n")));

        JPAQuery<Long> countQuery = queryFactory
                .select(releaseInfo.count())
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(getSortCondition(userId, homeSort, now), getCategoryCondition(category));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder getSortCondition(long userId, HomeSort homeSort, LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        BooleanBuilder builder = new BooleanBuilder();

        switch (homeSort) {
            case DUE_TODAY -> {
                //마감일 = 오늘, 발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
                builder.and(isSameDay(now, releaseInfo.dueDate)).and(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate));
            }
            case RELEASE_NOW -> {
                //발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
                builder.and(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate));
            }
            case RELEASE_WISH -> {
                //발매일 < 지금 < 마감일, 사용자의 관심 상품, 정렬은 마감일 오름차순
                builder.and(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate)).and(item.id.in(wishItemIdListByUser(userId)));
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

    private OrderSpecifier getSortOrder(HomeSort homeSort) {
        return switch (homeSort) {
            case REGISTER_TODAY -> new OrderSpecifier<>(Order.ASC, releaseInfo.createdAt);
            case RELEASE_CONFIRM -> new OrderSpecifier<>(Order.ASC, releaseInfo.releaseDate);
            default -> new OrderSpecifier<>(Order.ASC, releaseInfo.dueDate);
        };
    }

    private Set<Long> wishItemIdListByUser(long userId) {
        return new HashSet<>(queryFactory.select(wishItem.item.id)
                .from(wishItem)
                .where(wishItem.user.id.eq(userId))
                .fetch());
    }

    private BooleanExpression isSameDay(LocalDateTime dateTime, DateTimePath<LocalDateTime> datePath) {
        log.debug("datePath : {}", datePath);
        return Expressions.numberTemplate(Integer.class, "YEAR({0})", datePath).eq(dateTime.getYear())
                .and(Expressions.numberTemplate(Integer.class, "MONTH({0})", datePath).eq(dateTime.getMonthValue()))
                .and(Expressions.numberTemplate(Integer.class, "DAY({0})", datePath).eq(dateTime.getDayOfMonth()));
    }
}
