package com.ward.ward_server.api.releaseInfo.repository.query;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.DateTimeTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<ReleaseInfoSimpleResponse> getDueTodayReleaseInfoOrdered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //마감일 = 오늘, 발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
        return queryFactory.select(
                        Projections.constructor(ReleaseInfoSimpleResponse.class,
                                releaseInfo.id,
                                drawPlatform.koreanName,
                                drawPlatform.englishName,
                                item.id,
                                item.mainImage,
                                item.koreanName,
                                item.englishName,
                                releaseInfo.releaseMethod,
                                releaseInfo.dueDate
                        ))
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(isSameDay(now, releaseInfo.dueDate), nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
                .orderBy(releaseInfo.dueDate.asc())
                .limit(HOME_PAGE_SIZE)
                .fetch();
    }

    public List<ReleaseInfoSimpleResponse> getReleaseTodayReleaseInfoOrdered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //발매일 < 지금 < 마감일, 정렬은 마감일 오름차순
        return queryFactory.select(
                        Projections.constructor(ReleaseInfoSimpleResponse.class,
                                releaseInfo.id,
                                drawPlatform.koreanName,
                                drawPlatform.englishName,
                                item.id,
                                item.mainImage,
                                item.koreanName,
                                item.englishName,
                                releaseInfo.releaseMethod,
                                releaseInfo.dueDate
                        ))
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
                .orderBy(releaseInfo.dueDate.asc())
                .limit(HOME_PAGE_SIZE)
                .fetch();
    }

    public List<ReleaseInfoSimpleResponse> getWishItemReleaseInfoOrdered(long userId, LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //발매일 < 지금 < 마감일, 사용자의 관심 상품, 정렬은 마감일 오름차순
        return queryFactory.select(
                        Projections.constructor(ReleaseInfoSimpleResponse.class,
                                releaseInfo.id,
                                drawPlatform.koreanName,
                                drawPlatform.englishName,
                                item.id,
                                item.mainImage,
                                item.koreanName,
                                item.englishName,
                                releaseInfo.releaseMethod,
                                releaseInfo.dueDate
                        ))
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(item.id.in(wishItemIdListByUser(userId)), nowDateTime.between(releaseInfo.releaseDate, releaseInfo.dueDate))
                .orderBy(releaseInfo.dueDate.asc())
                .limit(HOME_PAGE_SIZE)
                .fetch();
    }
    public List<ReleaseInfoSimpleResponse> getJustConfirmReleaseInfoOrdered(LocalDateTime now) {
        DateTimeTemplate<LocalDateTime> nowDateTime = Expressions.dateTimeTemplate(LocalDateTime.class, "{0}", now);
        //지금 < 발매일, 정렬은 발매일 오름차순
        return queryFactory.select(
                        Projections.constructor(ReleaseInfoSimpleResponse.class,
                                releaseInfo.id,
                                drawPlatform.koreanName,
                                drawPlatform.englishName,
                                item.id,
                                item.mainImage,
                                item.koreanName,
                                item.englishName,
                                releaseInfo.releaseMethod,
                                releaseInfo.dueDate
                        ))
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(nowDateTime.before(releaseInfo.releaseDate))
                .orderBy(releaseInfo.releaseDate.asc())
                .limit(HOME_PAGE_SIZE)
                .fetch();
    }

    public List<ReleaseInfoSimpleResponse> getRegisterTodayReleaseInfoOrdered(LocalDateTime now) {
        //생성일 = 지금, 발매일 = null, 정렬은 생성일 오름차순
        return queryFactory.select(
                        Projections.constructor(ReleaseInfoSimpleResponse.class,
                                releaseInfo.id,
                                drawPlatform.koreanName,
                                drawPlatform.englishName,
                                item.id,
                                item.mainImage,
                                item.koreanName,
                                item.englishName,
                                releaseInfo.releaseMethod,
                                releaseInfo.dueDate
                        ))
                .from(releaseInfo)
                .leftJoin(releaseInfo.drawPlatform, drawPlatform)
                .leftJoin(releaseInfo.item, item)
                .leftJoin(item.brand, brand)
                .where(isSameDay(now, releaseInfo.createdAt))
                .orderBy(releaseInfo.createdAt.asc())
                .limit(HOME_PAGE_SIZE)
                .fetch();
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
