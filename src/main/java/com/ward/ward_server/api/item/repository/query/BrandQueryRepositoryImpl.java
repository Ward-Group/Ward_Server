package com.ward.ward_server.api.item.repository.query;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.dto.BrandItemResponse;
import com.ward.ward_server.api.item.repository.query.BrandQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;
import static com.ward.ward_server.api.wishBrand.QWishBrand.wishBrand;
import static com.ward.ward_server.api.wishItem.QWishItem.wishItem;

@RequiredArgsConstructor
@Slf4j
public class BrandQueryRepositoryImpl implements BrandQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<BrandInfoResponse> getBrandItemPage(Pageable pageable) {
        log.debug("pageable {}", pageable.toString());
        List<Tuple> content = queryFactory.select(
                        brand.id,
                        brand.logoImage,
                        brand.koreanName,
                        brand.englishName,
                        brand.viewCount,
                        count(wishBrand))
                .from(brand)
                .leftJoin(wishBrand).on(brand.id.eq(wishBrand.brand.id))
                .groupBy(brand.id)
                .orderBy(brand.viewCount.add(count(wishBrand)).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.debug("content size {}", content.size());
        log.debug("\n결과: {}", content.stream()
                .map(e -> e.toString())
                .collect(Collectors.joining("\n")));

        List<BrandInfoResponse> result = content.stream()
                .map(e -> new BrandInfoResponse(
                        e.get(brand.logoImage),
                        e.get(brand.koreanName),
                        e.get(brand.englishName),
                        e.get(brand.viewCount),
                        e.get(5, Long.class),
                        queryFactory.select(Projections.constructor(BrandItemResponse.class,
                                        item.koreanName,
                                        item.englishName,
                                        item.code,
                                        item.mainImage,
                                        item.viewCount,
                                        count(wishItem)))
                                .from(item)
                                .leftJoin(wishItem).on(item.id.eq(wishItem.item.id))
                                .where(item.brand.id.eq(e.get(brand.id)))
                                .groupBy(item.id)
                                .orderBy(item.viewCount.add(count(wishItem)).desc())
                                .limit(3)
                                .fetch()))
                .toList();

        JPAQuery<Long> countQuery = queryFactory
                .select(brand.count())
                .from(brand);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }
}
