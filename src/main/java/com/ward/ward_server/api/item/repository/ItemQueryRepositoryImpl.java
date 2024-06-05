package com.ward.ward_server.api.item.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enumtype.ItemSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.firefox.UnableToCreateProfileException;

import java.time.LocalDateTime;
import java.util.List;

import static com.querydsl.core.types.dsl.Expressions.dateTemplate;
import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;
import static com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo.releaseInfo;
import static com.ward.ward_server.api.wishItem.QWishItem.wishItem;

@RequiredArgsConstructor
@Slf4j
public class ItemQueryRepositoryImpl implements ItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    /* TODO
     * 한국시간 : UTC + 9
     * DB UTC기준으로 들어감.
     * 한국 플랫폼에서 응모할거임.
     * oracle 표준 시각 6/4 -> 6/3
     * timezone
     * from -> where -> select 한국시간 */
    public List<ItemSimpleResponse> getDueTodayItem10Ordered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        log.info("지금!!!:"+nowDateTime.toString());
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
                .where(isToday(nowDateTime, releaseInfo.dueDate), nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
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
                .where(isToday(nowDateTime, item.createdAt))
                .orderBy(item.createdAt.asc())
                .limit(10)
                .fetch();
    }
    private BooleanExpression isToday(DateTimeTemplate<LocalDateTime> now,DateTimePath<LocalDateTime> date) {
        return dateTemplate(Long.class, "datediff({0}, {1})", now, date).eq(0L);
    }
}
