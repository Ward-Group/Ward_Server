package com.ward.ward_server.api.item.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;
import static com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo.releaseInfo;
import static com.ward.ward_server.api.wishItem.QWishItem.wishItem;

@RequiredArgsConstructor
@Slf4j
public class ItemQueryRepositoryImpl implements ItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<ItemSimpleResponse> getDueTodayItem10Ordered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //마감일 = 오늘, 발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
        return queryFactory.select(
                        Projections.constructor(ItemSimpleResponse.class,
                                item.name,
                                item.code,
                                item.mainImage,
                                brand.name
                        )).from(releaseInfo)
                .leftJoin(item).on(releaseInfo.itemId.eq(item.id))
                .leftJoin(item.brand, brand)
                .where(isSameDay(now, releaseInfo.dueDate), nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
                .orderBy(releaseInfo.dueDate.asc())
                .limit(10)
                .fetch();
    }

    public List<ItemSimpleResponse> getReleaseTodayItem10Ordered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
        return queryFactory.select(
                        Projections.constructor(ItemSimpleResponse.class,
                                item.name,
                                item.code,
                                item.mainImage,
                                brand.name
                        )).from(releaseInfo)
                .leftJoin(item).on(releaseInfo.itemId.eq(item.id))
                .leftJoin(item.brand, brand)
                .where(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
                .orderBy(releaseInfo.dueDate.asc())
                .limit(10)
                .fetch();
    }

    public List<ItemSimpleResponse> getReleaseWishItem10Ordered(long userId, LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //발매일 < 지금 < 마감일, 사용자의 관심 상품, 정렬은 마감일 오름차순
        return queryFactory.select(
                        Projections.constructor(ItemSimpleResponse.class,
                                item.name,
                                item.code,
                                item.mainImage,
                                brand.name
                        )).from(releaseInfo)
                .leftJoin(item).on(releaseInfo.itemId.eq(item.id))
                .leftJoin(item.brand, brand)
                .where(item.id.in(
                                JPAExpressions.select(wishItem.item.id)
                                        .from(wishItem)
                                        .where(wishItem.user.id.eq(userId))),
                        nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
                .orderBy(releaseInfo.dueDate.asc())
                .limit(10)
                .fetch();
    }

    public List<ItemSimpleResponse> getNotReleaseItem10Ordered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //지금 < 발매일, 정렬은 발매일 오름차순
        return queryFactory.select(
                        Projections.constructor(ItemSimpleResponse.class,
                                item.name,
                                item.code,
                                item.mainImage,
                                brand.name
                        )).from(releaseInfo)
                .leftJoin(item).on(releaseInfo.itemId.eq(item.id))
                .leftJoin(item.brand, brand)
                .where(nowDateTime.before(releaseInfo.releaseDate))
                .orderBy(releaseInfo.releaseDate.asc())
                .limit(10)
                .fetch();
    }

    public List<ItemSimpleResponse> getRegisterTodayItem10Ordered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //생성일 = 지금, 발매일 = null, 정렬은 생성일 오름차순
        return queryFactory.select(
                        Projections.constructor(ItemSimpleResponse.class,
                                item.name,
                                item.code,
                                item.mainImage,
                                brand.name
                        )).from(item)
                .leftJoin(item.brand, brand)
                .where(isSameDay(now, item.createdAt))
                .orderBy(item.createdAt.asc())
                .limit(10)
                .fetch();
    }

    private BooleanExpression isSameDay(LocalDateTime now, DateTimePath<LocalDateTime> datePath) {
        return Expressions.numberTemplate(Integer.class, "YEAR({0})", datePath).eq(now.getYear())
                .and(Expressions.numberTemplate(Integer.class, "MONTH({0})", datePath).eq(now.getMonthValue()))
                .and(Expressions.numberTemplate(Integer.class, "DAY({0})", datePath).eq(now.getDayOfMonth()));
    }
}
