//package com.ward.ward_server.api.item.service;
//
//import com.ward.ward_server.api.item.entity.enums.Category;
//import com.ward.ward_server.api.item.repository.ItemRepository;
//import com.ward.ward_server.global.Object.enums.Section;
//import com.ward.ward_server.global.exception.ApiException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;
//import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
//
//@ExtendWith(MockitoExtension.class)
//class ItemServiceTest {
//    @Mock
//    ItemRepository itemRepository;
//    @InjectMocks
//    ItemService itemService;
//
//    @Test
//    void 제공하지_않는_섹션으로_접근시_예외를_발생한다_10List() {
//        assertThatExceptionOfType(ApiException.class)
//                .isThrownBy(() -> itemService.getItem10List(1L, Section.CLOSED, Category.FOOTWEAR))
//                .withMessage(INVALID_INPUT.getMessage());
//    }
//
//    @Test
//    void 제공하지_않는_섹션으로_접근시_예외를_발생한다_page() {
//        assertThatExceptionOfType(ApiException.class)
//                .isThrownBy(() -> itemService.getItemPage(1L, Section.REGISTER_TODAY, Category.FOOTWEAR, 1, "2024-07"))
//                .withMessage(INVALID_INPUT.getMessage());
//    }
//}