package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.dto.ItemSimpleResponse;
import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.entity.enumtype.Category;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.wishItem.WishItem;
import com.ward.ward_server.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Commit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class ItemRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    ItemRepository itemRepository;
    User user;

    @BeforeEach
    public void before() {
        user = new User("이름", "이메일", "비밀번호", "닉네임", true, true, true);
        em.persist(user);
    }

    @Test
    public void 오늘_마감인_상품_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "플랫폼이미지");
        em.persist(drawPlatform);
        for (int itemId = 1; itemId <= 10; itemId++) {
            // 마감일 = 오늘, 발매일 < 지금 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.minusDays(3), now.plusMinutes(itemId), now.plusDays(3), Status.POSSIBLE));
            // 마감일 = 오늘, 발매일 < 지금 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소", now.minusDays(3), now.plusMinutes(itemId), now.plusDays(3), Status.POSSIBLE));
            // 마감일 = 오늘, 발매일 < 마감일 < 지금 < 발표일
            em.persist(new ReleaseInfo(itemId + 20, drawPlatform, "주소", now.minusDays(3), now.minusMinutes(itemId), now.plusDays(3), Status.POSSIBLE));
        }
        em.flush();
        em.clear();

        //when
        List<ItemSimpleResponse> result = itemRepository.getDueTodayItemOrdered(user.getId(), now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ItemSimpleResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.size()).isEqualTo(10);
        assertThat(result).extracting("itemKoreanName").doesNotContain(
                IntStream.rangeClosed(1, 30)
                        .boxed()
                        .filter(e -> (6 <= e && e < 11) || 16 <= e)
                        .map(e -> "상품이름" + e)
                        .toArray());
    }

    @Test
    public void 현재_발매중인_상품_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "플랫폼이미지");
        em.persist(drawPlatform);
        for (int itemId = 1; itemId <= 5; itemId++) {
            // 지금 < 발매일 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.plusDays(itemId), now.plusDays(itemId + 3), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 발매일 < 마감일 < 지금 < 발표일
            em.persist(new ReleaseInfo(itemId + 5, drawPlatform, "주소", now.minusDays(itemId + 3), now.minusDays(itemId), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 발매일 < 지금 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소", now.minusDays(itemId), now.plusMinutes(itemId), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 발매일 < 지금 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 15, drawPlatform, "주소", now.minusDays(itemId), now.plusMinutes(itemId), now.plusDays(itemId + 5), Status.POSSIBLE));
        }
        em.flush();
        em.clear();

        //when
        List<ItemSimpleResponse> result = itemRepository.getReleaseTodayItemOrdered(user.getId(), now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ItemSimpleResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.size()).isEqualTo(10);
        assertThat(result).extracting("itemKoreanName").contains(
                IntStream.rangeClosed(11, 20)
                        .boxed()
                        .map(e -> "상품이름" + e)
                        .toArray());
    }

    @Test
    public void 관심상품_중에_현재_발매중인_상품_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "플랫폼이미지");
        em.persist(drawPlatform);

        for (long itemId = 5; itemId <= 15; itemId++)
            em.persist(new WishItem(user, itemRepository.findById(itemId).get()));
        for (int itemId = 1; itemId <= 5; itemId++) {
            // 지금 < 발매일 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.plusDays(itemId), now.plusDays(itemId + 3), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 발매일 < 마감일 < 지금 < 발표일
            em.persist(new ReleaseInfo(itemId + 5, drawPlatform, "주소", now.minusDays(itemId + 3), now.minusDays(itemId), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 발매일 < 지금 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소", now.minusDays(itemId), now.plusMinutes(itemId), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 발매일 < 지금 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 15, drawPlatform, "주소", now.minusDays(itemId), now.plusMinutes(itemId), now.plusDays(itemId + 5), Status.POSSIBLE));
        }
        em.flush();
        em.clear();

        //when
        List<ItemSimpleResponse> result = itemRepository.getReleaseWishItemOrdered(user.getId(), now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ItemSimpleResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.size()).isEqualTo(5);
        assertThat(result).extracting("itemKoreanName").contains(
                IntStream.rangeClosed(11, 15)
                        .boxed()
                        .map(e -> "상품이름" + e)
                        .toArray());
    }

    @Test
    public void 발매날짜는_확정이지만_미발매인_상품_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "플랫폼이미지");
        em.persist(drawPlatform);
        for (int itemId = 1; itemId <= 10; itemId++) {
            // 지금 < 발매일 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.plusDays(itemId), now.plusDays(itemId + 3), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 지금 < 발매일 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소" + itemId, now.plusDays(itemId), now.plusDays(itemId + 3), now.plusDays(itemId + 5), Status.POSSIBLE));
            // 발매일 < 지금 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId + 20, drawPlatform, "주소", now.minusDays(itemId), now.plusMinutes(itemId), now.plusDays(itemId + 5), Status.POSSIBLE));
        }
        em.flush();
        em.clear();

        //when
        List<ItemSimpleResponse> result = itemRepository.getJustConfirmReleaseItemOrdered(user.getId(), now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ItemSimpleResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.size()).isEqualTo(10);
        assertThat(result).extracting("itemKoreanName").doesNotContain(
                IntStream.rangeClosed(1, 30)
                        .boxed()
                        .filter(e -> (6 <= e && e < 11) || 16 <= e)
                        .map(e -> "상품이름" + e)
                        .toArray());
    }

    @Test
    public void 오늘_등록하고_미발매인_상품_조회() {
        //given
        Brand brand = new Brand("로고", "브랜드", "brand");
        em.persist(brand);
        for (int itemId = 1; itemId <= 10; itemId++) {
            em.persist(Item.builder()
                    .koreanName("상품이름" + itemId)
                    .code("상품코드" + itemId)
                    .brand(brand)
                    .category(Category.FOOTWEAR).build());
            //정렬 확인용
            em.persist(Item.builder()
                    .koreanName("상품이름" + (itemId + 10))
                    .code("상품코드" + (itemId + 10))
                    .brand(brand)
                    .category(Category.FOOTWEAR).build());
        }
        em.flush();
        em.clear();

        //when
        List<ItemSimpleResponse> result = itemRepository.getRegisterTodayItemOrdered(user.getId(), LocalDateTime.now());

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ItemSimpleResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.size()).isEqualTo(10);
        assertThat(result).extracting("itemKoreanName").doesNotContain(
                IntStream.rangeClosed(1, 20)
                        .boxed()
                        .filter(e -> (6 <= e && e < 11) || 16 <= e)
                        .map(e -> "상품이름" + e)
                        .toArray());
    }
}