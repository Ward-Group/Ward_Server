package com.ward.ward_server.api.wishBrand.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.wishBrand.WishBrandResponse;
import com.ward.ward_server.global.Object.enums.ApiSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.wishBrand.QWishBrand.wishBrand;

@RequiredArgsConstructor
@Slf4j
public class WishBrandQueryRepositoryImpl implements WishBrandQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<WishBrandResponse> getWishBrandPage(long userId, ApiSort apiSort, Pageable pageable) {
        Map<Long, Long> brandWishCountMap=countBrandWish();
        log.info("wish count brand {}", brandWishCountMap);
        List<WishBrandResponse> result = queryFactory.select(
                        Projections.constructor(WishBrandResponse.class,
                                brand.id,
                                brand.logoImage,
                                brand.koreanName,
                                brand.englishName)
                ).from(wishBrand)
                .leftJoin(wishBrand.brand, brand)
                .where(wishBrand.user.id.eq(userId))
                .groupBy(wishBrand.id)
                .orderBy(createOrderSpecifier(apiSort, brandWishCountMap))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.debug("content size {}", result.size());
        log.debug("\n결과: {}", result.stream()
                .map(e -> e.toString())
                .collect(Collectors.joining("\n")));

        JPAQuery<Long> countQuery = queryFactory
                .select(wishBrand.count())
                .from(wishBrand)
                .where(wishBrand.user.id.eq(userId));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier createOrderSpecifier(ApiSort apiSort, Map<Long, Long> map) {
        return switch (apiSort) {
            case KOREAN_ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, brand.koreanName);
            case ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, brand.englishName);
            default ->  new OrderSpecifier<>(Order.DESC, brand.viewCount);
        };
    }

    private Map<Long, Long> countBrandWish() {
        List<Tuple> tuples = queryFactory.select(
                        wishBrand.brand.id,
                        count(wishBrand.id)
                )
                .from(wishBrand)
                .groupBy(wishBrand.brand.id)
                .fetch();
        return tuples.stream()
                .collect(Collectors.toMap(
                        e->e.get(wishBrand.brand.id),
                        e->e.get(1, Long.class)
                ));
    }
}
