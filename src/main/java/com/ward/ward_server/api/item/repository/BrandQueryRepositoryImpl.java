package com.ward.ward_server.api.item.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.querydsl.core.types.ExpressionUtils.count;
import static com.ward.ward_server.api.item.entity.QBrand.brand;
import static com.ward.ward_server.api.item.entity.QItem.item;

@RequiredArgsConstructor
@Slf4j
public class BrandQueryRepositoryImpl implements BrandQueryRepository{
    private final JPAQueryFactory queryFactory;
    public Page<BrandInfoResponse> getBrandItemPage(int page, int size){
//        List<BrandInfoResponse> content = queryFactory
//                .select(Projections.constructor(BrandInfoResponse.class,
//                        brand.logoImage,
//                        brand.koreanName))
//                .from(brand)
//                .leftJoin(item).on(brand.id.eq(item.brand.id))
//                .where(usernameEq(condition.username()),
//                        teamNameEq(condition.teamName()),
//                        ageGoe(condition.ageGoe()),
//                        ageLoe(condition.ageLoe()))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        JPAQuery<Long> countQuery = queryFactory
//                .select(count(member))
//                .from(member)
//                .leftJoin(member.team, team)
//                .where(usernameEq(condition.username()),
//                        teamNameEq(condition.teamName()),
//                        ageGoe(condition.ageGoe()),
//                        ageLoe(condition.ageLoe()));

        //return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
        return null;
    }
}
