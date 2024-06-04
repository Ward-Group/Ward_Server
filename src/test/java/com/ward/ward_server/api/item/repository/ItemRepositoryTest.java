package com.ward.ward_server.api.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class ItemRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    JPAQueryFactory jpaQueryFactory;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 오늘_마감인_상품_가져오기(){
        LocalDateTime now=LocalDateTime.now();
        System.out.println("now:"+now);
        DrawPlatform drawPlatform=new DrawPlatform("플랫폼11", "플랫폼이미지11");
        em.persist(drawPlatform);
        for (int i=1; i<=10; i++){
            ReleaseInfo releaseInfo = new ReleaseInfo(i, drawPlatform, "주소:상품" + i + "-플랫폼11", now.minusDays(3), now.plusMinutes(i), now.plusDays(3), Status.POSSIBLE);
            ReleaseInfo releaseInfo2 = new ReleaseInfo(i+10, drawPlatform, "주소:상품" + i+10 + "-플랫폼11", now.minusDays(3), now.plusMinutes(i), now.plusDays(3), Status.POSSIBLE);
            em.persist(releaseInfo);
            em.persist(releaseInfo2);
        }
        em.flush();
        em.clear();
        List<ItemSimpleResponse> result = itemRepository.getDueTodayItem10Ordered();
        System.out.println("결과:");
        for(ItemSimpleResponse response:result){
            System.out.println(response);
        }
        assertThat(result.size()).isEqualTo(10);
    }
}