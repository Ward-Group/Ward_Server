package com.ward.ward_server.api.item.repository.query;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.dto.BrandItemMainImageResponse;
import com.ward.ward_server.global.Object.enums.BasicSort;
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

    public Page<BrandInfoResponse> getBrandAndItem3Page(BasicSort basicSort, Pageable pageable) {
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
                .orderBy(createOrderSpecifier(basicSort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BrandInfoResponse> result = content.stream()
                .map(e -> new BrandInfoResponse(
                        e.get(brand.id),
                        e.get(brand.logoImage),
                        e.get(brand.koreanName),
                        e.get(brand.englishName),
                        e.get(brand.viewCount),
                        e.get(5, Long.class),
                        queryFactory.select(Projections.constructor(BrandItemMainImageResponse.class,
                                        item.id,
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

    private OrderSpecifier createOrderSpecifier(BasicSort basicSort){
        return switch (basicSort){
            case KOREAN_ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, brand.koreanName);
            case ALPHABETICAL -> new OrderSpecifier<>(Order.ASC, brand.englishName);
            default -> new OrderSpecifier<>(Order.DESC, brand.viewCount.add(count(wishBrand)));
        };
    }
}
