//package com.ward.ward_server.api.search.service;
//
//import com.ward.ward_server.api.item.entity.Brand;
//import com.ward.ward_server.api.item.entity.Item;
//import com.ward.ward_server.api.item.repository.ItemRepository;
//import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
//import com.ward.ward_server.api.search.dto.SearchItemsResponse;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.Collections;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class SearchServiceTest {
//
//    @InjectMocks
//    private SearchService searchService;
//
//    @Mock
//    private ItemRepository itemRepository;
//
//    @Mock
//    private ReleaseInfoRepository releaseInfoRepository;
//
//    @Test
//    public void testSearchItems_Success() {
//        Item item = mock(Item.class);
//        when(item.getId()).thenReturn(1L);
//        when(item.getMainImage()).thenReturn("mainImage");
//        when(item.getKoreanName()).thenReturn("koreanName");
//        when(item.getEnglishName()).thenReturn("englishName");
//        Brand brand = mock(Brand.class);
//        when(brand.getKoreanName()).thenReturn("brandName");
//        when(item.getBrand()).thenReturn(brand);
//        when(item.getViewCount()).thenReturn(100L);
//
//        Page<Item> items = new PageImpl<>(Collections.singletonList(item));
//        when(itemRepository.searchItems(anyString(), any(PageRequest.class))).thenReturn(items);
//        when(releaseInfoRepository.countByItemId(any(Long.class))).thenReturn(5);
//
//        SearchItemsResponse response = searchService.searchItems("keyword", 0, 10);
//
//        assertEquals(1L, response.getTotalCount());
//        assertEquals(1, response.getResults().size());
//        assertEquals(5, response.getResults().get(0).getReleaseCount());
//    }
//}
