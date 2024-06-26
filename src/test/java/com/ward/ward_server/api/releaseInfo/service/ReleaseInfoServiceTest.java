package com.ward.ward_server.api.releaseInfo.service;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.entity.DrawPlatform;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.entity.enums.CurrencyUnit;
import com.ward.ward_server.api.releaseInfo.entity.enums.DeliveryMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.NotificationMethod;
import com.ward.ward_server.api.releaseInfo.entity.enums.ReleaseMethod;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.global.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReleaseInfoServiceTest {

    @Mock
    private ReleaseInfoRepository releaseInfoRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ReleaseInfoService releaseInfoService;

    private Item item;

    @BeforeEach
    public void setUp() {
        try (AutoCloseable mocks = MockitoAnnotations.openMocks(this)) {
            item = Item.builder().code("itemCode").koreanName("itemKoreanName").englishName("itemEnglishName").build();
            setId(item, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setId(Item item, Long id) {
        try {
            java.lang.reflect.Field field = Item.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(item, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getOngoingReleaseInfos_success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(releaseInfoRepository.findByItemAndDueDateAfter(any(Item.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(new ReleaseInfo())));

        Page<ReleaseInfo> result = releaseInfoService.getOngoingReleaseInfos(1L, 0, 10);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void getOngoingReleaseInfos_itemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> releaseInfoService.getOngoingReleaseInfos(1L, 0, 10));
    }

    @Test
    public void getCompletedReleaseInfos_success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(releaseInfoRepository.findByItemAndDueDateBefore(any(Item.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(new ReleaseInfo())));

        Page<ReleaseInfo> result = releaseInfoService.getCompletedReleaseInfos(1L, 0, 10);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void getCompletedReleaseInfos_itemNotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> releaseInfoService.getCompletedReleaseInfos(1L, 0, 10));
    }

    @Test
    public void getOngoingReleaseInfos_multipleResults() {
        // Arrange
        ReleaseInfo releaseInfo1 = ReleaseInfo.builder()
                .item(item)
                .drawPlatform(DrawPlatform.builder().koreanName("Platform1").englishName("Platform1").logoImage("logo1").build())
                .siteUrl("http://example1.com")
                .releaseDate("2023-06-01 00:00")
                .dueDate("2023-07-01 00:00")
                .presentationDate("2023-08-01 00:00")
                .releasePrice(100)
                .currencyUnit(CurrencyUnit.KRW)
                .notificationMethod(NotificationMethod.EMAIL)
                .releaseMethod(ReleaseMethod.ENTRY)
                .deliveryMethod(DeliveryMethod.DOMESTIC)
                .build();

        ReleaseInfo releaseInfo2 = ReleaseInfo.builder()
                .item(item)
                .drawPlatform(DrawPlatform.builder().koreanName("Platform2").englishName("Platform2").logoImage("logo2").build())
                .siteUrl("http://example2.com")
                .releaseDate("2023-06-01 00:00")
                .dueDate("2023-07-01 00:00")
                .presentationDate("2023-08-01 00:00")
                .releasePrice(200)
                .currencyUnit(CurrencyUnit.USD)
                .notificationMethod(NotificationMethod.APP_NOTIFICATION)
                .releaseMethod(ReleaseMethod.FIRST_COME)
                .deliveryMethod(DeliveryMethod.DIRECT)
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(releaseInfoRepository.findByItemAndDueDateAfter(any(Item.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(releaseInfo1, releaseInfo2)));

        // Act
        Page<ReleaseInfo> result = releaseInfoService.getOngoingReleaseInfos(1L, 0, 10);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(releaseInfo1, result.getContent().get(0));
        assertEquals(releaseInfo2, result.getContent().get(1));
    }

    @Test
    public void getCompletedReleaseInfos_multipleResults() {
        // Arrange
        ReleaseInfo releaseInfo1 = ReleaseInfo.builder()
                .item(item)
                .drawPlatform(DrawPlatform.builder().koreanName("Platform1").englishName("Platform1").logoImage("logo1").build())
                .siteUrl("http://example1.com")
                .releaseDate("2023-06-01 00:00")
                .dueDate("2023-06-15 00:00")
                .presentationDate("2023-07-01 00:00")
                .releasePrice(100)
                .currencyUnit(CurrencyUnit.KRW)
                .notificationMethod(NotificationMethod.EMAIL)
                .releaseMethod(ReleaseMethod.ENTRY)
                .deliveryMethod(DeliveryMethod.DOMESTIC)
                .build();

        ReleaseInfo releaseInfo2 = ReleaseInfo.builder()
                .item(item)
                .drawPlatform(DrawPlatform.builder().koreanName("Platform2").englishName("Platform2").logoImage("logo2").build())
                .siteUrl("http://example2.com")
                .releaseDate("2023-06-01 00:00")
                .dueDate("2023-06-15 00:00")
                .presentationDate("2023-07-01 00:00")
                .releasePrice(200)
                .currencyUnit(CurrencyUnit.USD)
                .notificationMethod(NotificationMethod.APP_NOTIFICATION)
                .releaseMethod(ReleaseMethod.FIRST_COME)
                .deliveryMethod(DeliveryMethod.DIRECT)
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(releaseInfoRepository.findByItemAndDueDateBefore(any(Item.class), any(LocalDateTime.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(releaseInfo1, releaseInfo2)));

        // Act
        Page<ReleaseInfo> result = releaseInfoService.getCompletedReleaseInfos(1L, 0, 10);

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(releaseInfo1, result.getContent().get(0));
        assertEquals(releaseInfo2, result.getContent().get(1));
    }
}
