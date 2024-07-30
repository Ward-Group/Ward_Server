//package com.ward.ward_server.api.releaseInfo.repository;
//
//import com.ward.ward_server.api.item.entity.Brand;
//import com.ward.ward_server.api.item.entity.Item;
//import com.ward.ward_server.api.item.entity.enums.Category;
//import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;
//import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
//import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
//import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
//import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
//import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
//import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
//import com.ward.ward_server.api.user.entity.User;
//import com.ward.ward_server.api.wishItem.WishItem;
//import com.ward.ward_server.global.Object.enums.Section;
//import com.ward.ward_server.global.config.QuerydslConfig;
//import jakarta.persistence.EntityManager;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.IntStream;
//
//import static com.ward.ward_server.global.Object.Constants.*;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@Import(QuerydslConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Slf4j
//class ReleaseInfoRepositoryTest {
//    @Autowired
//    EntityManager em;
//    @Autowired
//    ReleaseInfoRepository releaseInfoRepository;
//    List<Item> items = new ArrayList<>();
//    User user;
//    Brand brand;
//    DrawPlatform drawPlatform;
//
//    @BeforeEach
//    public void before() {
//        user = new User("이름", "이메일", "비밀번호", "닉네임", true, true, true);
//        em.persist(user);
//
//        brand = new Brand("로고이미지", "브랜드이름", "englishName");
//        em.persist(brand);
//
//        drawPlatform = new DrawPlatform("테스트플랫폼", "testPlatform", "플랫폼이미지");
//        em.persist(drawPlatform);
//
//        //index 0~29는 FOOTWEAR 상품이다.
//        for (int i = 1; i <= 30; i++) {
//            Item item = new Item("상품코드", "상품이름" + i, "englishName", "메인이미지", brand, Category.FOOTWEAR, 10000);
//            em.persist(item);
//            items.add(item);
//        }
//        //index 30~59는 CLOTHING 상품이다.
//        for (int i = 31; i <= 60; i++) {
//            Item item = new Item("상품코드", "상품이름" + i, "englishName", "메인이미지", brand, Category.CLOTHING, 10000);
//            em.persist(item);
//            items.add(item);
//        }
//    }
//
//    @Test
//    public void FOOTWEAR_상품_중에서_오늘_마감인_상품의_발매정보를_가져온다() {
//        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
//        for (int itemId = 1; itemId <= 10; itemId++) {
//            // 마감일 = 오늘, 발매일 < 지금 < 마감일 < 발표일 (조건 만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.minusDays(3).format(DATE_STRING_FORMAT), now.plusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(3).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 마감일 = 오늘, 발매일 < 지금 < 마감일 < 발표일 (조건 만족), 정렬 확인용
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 10), drawPlatform, "주소", now.minusDays(3).format(DATE_STRING_FORMAT), now.plusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(3).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 마감일 = 오늘, 발매일 < 마감일 < 지금 < 발표일 (조건 불만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 20), drawPlatform, "주소", now.minusDays(3).format(DATE_STRING_FORMAT), now.minusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(3).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//        }
//        em.flush();
//        em.clear();
//
//        //when
//        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseInfo10List(user.getId(), now, Category.FOOTWEAR, Section.DUE_TODAY);
//
//        //then
//        assertThat(result.size()).isEqualTo(10);
//        //1~30 중에서 6~10, 16~30은 제외한다. (= 1~5, 11~15는 포함한다)
//        assertThat(result).extracting("itemKoreanName").doesNotContain(
//                IntStream.rangeClosed(1, 30)
//                        .boxed()
//                        .filter(e -> (6 <= e && e < 11) || 16 <= e)
//                        .map(e -> "상품이름" + e)
//                        .toArray());
//        //정렬 확인한다.
//        assertThat(result.get(0).itemKoreanName()).isEqualTo("상품이름1");
//        assertThat(result.get(1).itemKoreanName()).isEqualTo("상품이름11");
//    }
//
//    @Test
//    public void FOOTWEAR_상품_중에서_현재_발매중인_발매정보를_가져온다() {
//        //given
//        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
//        for (int itemId = 1; itemId <= 5; itemId++) {
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 불만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 발매일 < 마감일 < 지금 < 발표일 (조건 불만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 5), drawPlatform, "주소", now.minusDays(itemId + 3).format(DATE_STRING_FORMAT), now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 발매일 < 지금 < 마감일 < 발표일 (조건 만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 10), drawPlatform, "주소", now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 발매일 < 지금 < 마감일 < 발표일 (조건 만족), 정렬 확인용
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 15), drawPlatform, "주소", now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//        }
//        em.flush();
//        em.clear();
//
//        //when
//        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseInfo10List(user.getId(), now, Category.FOOTWEAR, Section.RELEASE_NOW);
//
//        //then
//        assertThat(result.size()).isEqualTo(10);
//        //1~20 중에서 11~20를 포함한다.
//        assertThat(result).extracting("itemKoreanName").contains(
//                IntStream.rangeClosed(11, 20)
//                        .boxed()
//                        .map(e -> "상품이름" + e)
//                        .toArray());
//        //정렬 확인한다.
//        assertThat(result.get(0).itemKoreanName()).isEqualTo("상품이름11");
//        assertThat(result.get(1).itemKoreanName()).isEqualTo("상품이름16");
//    }
//
//    @Test
//    public void FOOTWEAR_관심상품_중에서_현재_발매중인_상품의_발매정보를_가져온다() {
//        //given
//        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
//
//        //index가 4~19인 상품이 관심상품이다.
//        for (int itemId = 5; itemId <= 20; itemId++) {
//            em.persist(new WishItem(user, items.get(itemId - 1)));
//        }
//        for (int itemId = 1; itemId <= 5; itemId++) {
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 불만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 발매일 < 마감일 < 지금 < 발표일 (조건 불만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 5), drawPlatform, "주소", now.minusDays(itemId + 3).format(DATE_STRING_FORMAT), now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 발매일 < 지금 < 마감일 < 발표일 (조건 만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 10), drawPlatform, "주소", now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 발매일 < 지금 < 마감일 < 발표일 (조건 만족), 정렬 확인용
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 15), drawPlatform, "주소", now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//        }
//        em.flush();
//        em.clear();
//
//        //when
//        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseInfo10List(user.getId(), now, Category.FOOTWEAR, Section.RELEASE_WISH);
//
//        //then
//        assertThat(result.size()).isEqualTo(10);
//        //1~20 중에서 11~20 상품을 포함한다.
//        assertThat(result).extracting("itemKoreanName").contains(
//                IntStream.rangeClosed(11, 20)
//                        .boxed()
//                        .map(e -> "상품이름" + e)
//                        .toArray());
//        //정렬 확인한다.
//        assertThat(result.get(0).itemKoreanName()).isEqualTo("상품이름11");
//        assertThat(result.get(1).itemKoreanName()).isEqualTo("상품이름16");
//    }
//
//
////    @Test
////    public void FOOTWEAR_상품_중에서_발매날짜가_확정됐지만_미발매인_상품의_발매정보를_가져온다() {
////        //given
////        LocalDateTime now = LocalDateTime.of(2024, 7, 3, 13, 30);
////        for (int itemId = 1; itemId <= 10; itemId++) {
////            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족)
////            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
////
////            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족), 정렬 확인용
////            em.persist(new ReleaseInfo(items.get(itemId - 1 + 10), drawPlatform, "주소" + itemId, now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
////
////            // 발매일 < 지금 < 마감일 < 발표일 (조건 불만족)
////            em.persist(new ReleaseInfo(items.get(itemId - 1 + 20), drawPlatform, "주소", now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusMinutes(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
////        }
////        em.flush();
////        em.clear();
////
////        //when
////        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseInfo10List(user.getId(), now, Category.FOOTWEAR, Section.RELEASE_SCHEDULE);
////
////        //then
////        assertThat(result.size()).isEqualTo(HOME_PAGE_SIZE);
////        //1~30 중에서 6~10, 16~30은 제외한다. (= 1~5, 11~15는 포함한다)
////        assertThat(result).extracting("itemKoreanName").doesNotContain(
////                IntStream.rangeClosed(1, 30)
////                        .boxed()
////                        .filter(e -> (6 <= e && e < 11) || 16 <= e)
////                        .map(e -> "상품이름" + e)
////                        .toArray());
////        //정렬 확인한다.
////        assertThat(result.get(0).itemKoreanName()).isEqualTo("상품이름1");
////        assertThat(result.get(1).itemKoreanName()).isEqualTo("상품이름11");
////    }
//
//    @Test
//    public void FOOTWEAR_상품_중에서_오늘_등록한_발매정보를_가져온다() {
//        //given
//        LocalDateTime now = LocalDateTime.now();
//        for (int itemId = 1; itemId <= 10; itemId++) {
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족)
//            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족), 정렬 확인용
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 10), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//        }
//        em.flush();
//        em.clear();
//
//        //when
//        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseInfo10List(user.getId(), now, Category.FOOTWEAR, Section.REGISTER_TODAY);
//
//        //then
//        assertThat(result.size()).isEqualTo(HOME_PAGE_SIZE);
//        //1~20 중에서 6~10, 16~20은 제외한다. (= 1~5, 11~15는 포함한다)
//        assertThat(result).extracting("itemKoreanName").doesNotContain(
//                IntStream.rangeClosed(1, 20)
//                        .boxed()
//                        .filter(e -> (6 <= e && e < 11) || 16 <= e)
//                        .map(e -> "상품이름" + e)
//                        .toArray());
//        //정렬 확인한다.
//        assertThat(result.get(0).itemKoreanName()).isEqualTo("상품이름1");
//        assertThat(result.get(1).itemKoreanName()).isEqualTo("상품이름11");
//    }
//
//    @Test
//    public void ALL_카테고리는_FOOTWEAR과_CLOTHING인_상품을_다_가져온다() {
//        //given
//        LocalDateTime now = LocalDateTime.now();
//        for (int itemId = 1; itemId <= 10; itemId++) {
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족), FOOTWEAR 상품
//            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족), CLOTHING 상품
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 30), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//        }
//        em.flush();
//        em.clear();
//
//        //when
//        List<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseInfo10List(user.getId(), now, Category.ALL, Section.REGISTER_TODAY);
//
//        //then
//        assertThat(result.size()).isEqualTo(HOME_PAGE_SIZE);
//        //1~40 중에서 6~30, 36~40는 제외한다. (= 1~5, 31~35는 포함한다)
//        assertThat(result).extracting("itemKoreanName").doesNotContain(
//                IntStream.rangeClosed(1, 40)
//                        .boxed()
//                        .filter(e -> (6 <= e && e < 31) || 35 < e)
//                        .map(e -> "상품이름" + e)
//                        .toArray());
//        //정렬 확인한다.
//        assertThat(result.get(0).itemKoreanName()).isEqualTo("상품이름1");
//        assertThat(result.get(1).itemKoreanName()).isEqualTo("상품이름31");
//    }
//
//    @Test
//    public void 총_발매정보의_개수가_total일때_마지막_페이지를_가져온다() {
//        //given
//        LocalDateTime now = LocalDateTime.now();
//
//        int total = 48;
//        for (int itemId = 1; itemId <= total / 2; itemId++) {
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족), FOOTWEAR 상품
//            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//            // 지금 < 발매일 < 마감일 < 발표일 (조건 만족), CLOTHING 상품
//            em.persist(new ReleaseInfo(items.get(itemId - 1 + 30), drawPlatform, "주소", now.plusDays(itemId).format(DATE_STRING_FORMAT), now.plusDays(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//        }
//        em.flush();
//        em.clear();
//
//        //when
//        int page = total % API_PAGE_SIZE == 0 ? (total / API_PAGE_SIZE) : (total / API_PAGE_SIZE + 1); //마지막 페이지 계산
//        Page<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getReleaseInfoPage(user.getId(), now, Category.ALL, Section.REGISTER_TODAY, PageRequest.of(page - 1, API_PAGE_SIZE));
//        //then
//        assertThat(result.getContent().size()).isEqualTo(total % API_PAGE_SIZE);
//        assertThat(result.getTotalElements()).isEqualTo(total);
//        assertThat(result.getNumber()).isEqualTo(page - 1);
//        assertThat(result.getTotalPages()).isEqualTo(page);
//        //마지막 값을 확인한다.
//        assertThat(result.getContent().get(total % API_PAGE_SIZE == 0 ? API_PAGE_SIZE - 1 : (total % API_PAGE_SIZE) - 1).itemKoreanName()).isEqualTo("상품이름" + (total / 2 + 30));
//    }
//
//    @Test
//    public void 마감순으로_정렬한_브랜드의_발매정보의_마지막_페이지를_가져온다() {
//        //given
//        LocalDateTime now = LocalDateTime.now();
//        int total = 48;
//        for (int itemId = 1; itemId <= total; itemId++) {
//            em.persist(new ReleaseInfo(items.get(itemId - 1), drawPlatform, "주소", now.minusDays(itemId).format(DATE_STRING_FORMAT), now.plusHours(itemId + 3).format(DATE_STRING_FORMAT), now.plusDays(itemId + 5).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//        }
//        //마지막 데이터
//        em.persist(new ReleaseInfo(items.get(total), drawPlatform, "주소", now.minusDays(total + 3).format(DATE_STRING_FORMAT), now.minusDays(total + 2).format(DATE_STRING_FORMAT), now.minusDays(total + 1).format(DATE_STRING_FORMAT), 30000, CurrencyUnit.KRW, NotificationMethod.EMAIL, ReleaseMethod.ENTRY, DeliveryMethod.AGENCY));
//
//        //when
//        int page = total % API_PAGE_SIZE == 0 ? (total / API_PAGE_SIZE) : (total / API_PAGE_SIZE + 1); //마지막 페이지 계산
//        Page<ReleaseInfoSimpleResponse> result = releaseInfoRepository.getBrandReleaseInfoPage(brand.getId(), PageRequest.of(page - 1, API_PAGE_SIZE));
//
//        //then
//        assertThat(result.getContent().size()).isEqualTo(total % API_PAGE_SIZE + 1);
//        assertThat(result.getTotalElements()).isEqualTo(total + 1);
//        assertThat(result.getNumber()).isEqualTo(page - 1);
//        assertThat(result.getTotalPages()).isEqualTo(page);
//        //마지막 값을 확인한다.
//        assertThat(result.getContent().get((total + 1) % API_PAGE_SIZE == 0 ? API_PAGE_SIZE - 1 : ((total + 1) % API_PAGE_SIZE) - 1).itemKoreanName()).isEqualTo("상품이름" + (total + 1));
//    }
//}