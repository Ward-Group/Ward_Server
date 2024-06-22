package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.wishItem.WishItem;
import com.ward.ward_server.global.config.QuerydslConfig;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ward.ward_server.global.Object.Constants.FORMAT;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
class ReleaseInfoRepositoryTest {
    @Autowired
    EntityManager em;
    @Autowired
    ReleaseInfoRepository releaseInfoRepository;
    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 오늘_마감인_발매정보_조회() {
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "testPlatform", "플랫폼이미지");
        em.persist(drawPlatform);
        for (int itemId = 1; itemId <= 10; itemId++) {
            // 마감일 = 오늘, 발매일 < 지금 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.minusDays(3).format(FORMAT), now.plusMinutes(itemId).format(FORMAT), now.plusDays(3).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 마감일 = 오늘, 발매일 < 지금 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소", now.minusDays(3).format(FORMAT), now.plusMinutes(itemId).format(FORMAT), now.plusDays(3).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 마감일 = 오늘, 발매일 < 마감일 < 지금 < 발표일
            em.persist(new ReleaseInfo(itemId + 20, drawPlatform, "주소", now.minusDays(3).format(FORMAT), now.minusMinutes(itemId).format(FORMAT), now.plusDays(3).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
        }
        em.flush();
        em.clear();

        //when
        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getDueTodayReleaseInfoOrdered(now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ReleaseInfoSimpleResponse::toString)
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
    public void 현재_발매중인_발매정보_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "testPlatform", "플랫폼이미지");
        em.persist(drawPlatform);
        for (int itemId = 1; itemId <= 5; itemId++) {
            // 지금 < 발매일 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.plusDays(itemId).format(FORMAT), now.plusDays(itemId + 3).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 마감일 < 지금 < 발표일
            em.persist(new ReleaseInfo(itemId + 5, drawPlatform, "주소", now.minusDays(itemId + 3).format(FORMAT), now.minusDays(itemId).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 지금 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소", now.minusDays(itemId).format(FORMAT), now.plusMinutes(itemId).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 지금 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 15, drawPlatform, "주소", now.minusDays(itemId).format(FORMAT), now.plusMinutes(itemId).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
        }
        em.flush();
        em.clear();

        //when
        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseTodayReleaseInfoOrdered(now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ReleaseInfoSimpleResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.size()).isEqualTo(10);
        assertThat(result).extracting("itemKoreanName").contains(
                IntStream.rangeClosed(11, 20)
                        .boxed()
                        .map(e -> "상품이름" + e)
                        .toArray());
    }

    @Test
    public void 관심상품_중에_현재_발매중인_상품의_발매정보_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        User user = new User("이름", "이메일", "비밀번호", "닉네임", true, true, true);
        em.persist(user);
        DrawPlatform drawPlatform1 = new DrawPlatform("테스트플랫폼1", "testPlatform1", "플랫폼이미지1");
        em.persist(drawPlatform1);
        DrawPlatform drawPlatform2 = new DrawPlatform("테스트플랫폼2", "testPlatform2", "플랫폼이미지2");
        em.persist(drawPlatform2);

        for (long itemId = 5; itemId <= 20; itemId++)
            em.persist(new WishItem(user, itemRepository.findById(itemId).get()));
        for (int itemId = 1; itemId <= 5; itemId++) {
            // 지금 < 발매일 < 마감일 < 발표일 (조건 불만족)
            em.persist(new ReleaseInfo(itemId, drawPlatform1, "주소" + itemId, now.plusDays(itemId).format(FORMAT), now.plusDays(itemId + 3).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 마감일 < 지금 < 발표일 (조건 불만족)
            em.persist(new ReleaseInfo(itemId + 5, drawPlatform1, "주소", now.minusDays(itemId + 3).format(FORMAT), now.minusDays(itemId).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 지금 < 마감일 < 발표일 (조건 만족) 플랫폼1
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform1, "주소", now.minusDays(itemId).format(FORMAT), now.plusMinutes(itemId + 10).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 지금 < 마감일 < 발표일 (조건 만족) 플랫폼2 / 마감일이 더 빠름 -> 플랫폼1보다 앞에 정렬
            em.persist(new ReleaseInfo(itemId + 13, drawPlatform2, "주소", now.minusDays(itemId).format(FORMAT), now.plusMinutes(itemId).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 지금 < 마감일 < 발표일, 정렬 확인용 (조건 만족) 플랫폼1
            em.persist(new ReleaseInfo(itemId + 15, drawPlatform1, "주소", now.minusDays(itemId).format(FORMAT), now.plusMinutes(itemId + 10).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
        }
        em.flush();
        em.clear();

        //when
        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getWishItemReleaseInfoOrdered(user.getId(), now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ReleaseInfoSimpleResponse::toString)
                .collect(Collectors.joining("\n")));

        assertThat(result.size()).isEqualTo(10);
        IntStream.of(1, 5)
                .forEach(e -> assertThat(result.get(e - 1).itemId()).isEqualTo(e + 13));
    }


    @Test
    public void 발매날짜는_확정이지만_미발매인_발매정보_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "testPlatform", "플랫폼이미지");
        em.persist(drawPlatform);
        for (int itemId = 1; itemId <= 10; itemId++) {
            // 지금 < 발매일 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.plusDays(itemId).format(FORMAT), now.plusDays(itemId + 3).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 지금 < 발매일 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소" + itemId, now.plusDays(itemId).format(FORMAT), now.plusDays(itemId + 3).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 발매일 < 지금 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId + 20, drawPlatform, "주소", now.minusDays(itemId).format(FORMAT), now.plusMinutes(itemId).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
        }
        em.flush();
        em.clear();

        //when
        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getJustConfirmReleaseInfoOrdered(now);

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ReleaseInfoSimpleResponse::toString)
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
    public void 오늘_등록한_발매정보_조회() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
        DrawPlatform drawPlatform = new DrawPlatform("테스트플랫폼", "testPlatform", "플랫폼이미지");
        em.persist(drawPlatform);
        for (int itemId = 1; itemId <= 10; itemId++) {
            // 지금 < 발매일 < 마감일 < 발표일
            em.persist(new ReleaseInfo(itemId, drawPlatform, "주소" + itemId, now.plusDays(itemId).format(FORMAT), now.plusDays(itemId + 3).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));

            // 지금 < 발매일 < 마감일 < 발표일, 정렬 확인용
            em.persist(new ReleaseInfo(itemId + 10, drawPlatform, "주소" + itemId, now.plusDays(itemId).format(FORMAT), now.plusDays(itemId + 3).format(FORMAT), now.plusDays(itemId + 5).format(FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
        }
        em.flush();
        em.clear();

        //when
        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getRegisterTodayReleaseInfoOrdered(LocalDateTime.now());

        //then
        log.debug("\n결과: {}", result.stream()
                .map(ReleaseInfoSimpleResponse::toString)
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