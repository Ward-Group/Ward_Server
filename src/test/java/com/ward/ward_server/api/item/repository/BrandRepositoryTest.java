package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.global.Object.enums.BasicSort;
import com.ward.ward_server.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class BrandRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    BrandRepository brandRepository;

    @Test
    public void 브랜드_top10_조회() {
        //given: test-data-init.sql
        int page = 0;
        int size = 10;
        long id = brandRepository.count() + 1; //db에 있는 브랜드 총 개수 + 1
        Brand brand = Brand.builder()
                .englishName("brandName" + id)
                .build();
        em.persist(brand);
        IntStream.range(0, 30).forEach(i -> brand.increaseViewCount()); //브랜드 조회수를 db 데이터보다 더 많게 설정 -> top1

        //when
        Page<BrandInfoResponse> result = brandRepository.getBrandAndItem3Page(BasicSort.RANKING, PageRequest.of(page, size));

        //then
        log.info("\n결과: {}", result.stream()
                .map(BrandInfoResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.getSize()).isEqualTo(size);
        assertThat(result.getNumber()).isEqualTo(page);
        assertThat(result.getContent().get(0).brandEnglishName()).isEqualTo("brandName" + id);
    }
}