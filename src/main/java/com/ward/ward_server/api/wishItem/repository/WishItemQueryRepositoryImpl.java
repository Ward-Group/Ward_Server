package com.ward.ward_server.api.wishItem.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.wishItem.WishItemResponse;
import com.ward.ward_server.global.Object.enums.ApiSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.ward.ward_server.api.entry.entity.QEntryRecord.entryRecord;
import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;
import static com.ward.ward_server.api.releaseInfo.entity.QReleaseInfo.releaseInfo;
import static com.ward.ward_server.api.wishItem.QWishItem.wishItem;

@RequiredArgsConstructor
@Slf4j
public class WishItemQueryRepositoryImpl implements WishItemQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<WishItemResponse> getWishItemPage(long userId, ApiSort apiSort, Pageable pageable) {
        List<Tuple> content = queryFactory.select(
                        brand.koreanName,
                        brand.englishName,
                        item.mainImage,
                        item.koreanName,
                        item.englishName,
                        item.id)
                .from(wishItem)
                .leftJoin(wishItem.item, item)
                .leftJoin(item.brand, brand)
                .where(wishItem.user.id.eq(userId))
                .groupBy(wishItem.id)
                .orderBy(createOrderSpecifier(apiSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.debug("content size {}", content.size());
        log.debug("\n결과: {}", content.stream()
                .map(e -> e.toString())
                .collect(Collectors.joining("\n")));

        List<WishItemResponse> result = content.stream()
                .map(e -> new WishItemResponse(
                        e.get(brand.koreanName),
                        e.get(brand.englishName),
                        e.get(item.id),
                        e.get(item.mainImage),
                        e.get(item.koreanName),
                        e.get(item.englishName),
                        entryItemIdsByUser(userId).contains(item.id)))
                .toList();

        JPAQuery<Long> countQuery = queryFactory
                .select(wishItem.count())
                .from(wishItem)
                .where(wishItem.user.id.eq(userId));

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private Set<Long> entryItemIdsByUser(long userId) {
        return new HashSet<>(
                queryFactory.select(
                                releaseInfo.item.id
                        ).from(entryRecord)
                        .leftJoin(entryRecord.releaseInfo, releaseInfo)
                        .where(entryRecord.user.id.eq(userId))
                        .fetch()
        );
    }

    private OrderSpecifier createOrderSpecifier(ApiSort apiSort) {
        return switch (apiSort) {
            case KOREAN_ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, item.koreanName);
            case ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, item.englishName);
            default -> new OrderSpecifier<>(Order.DESC, item.viewCount);
        };
    }
}
