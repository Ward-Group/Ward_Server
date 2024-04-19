package com.ward.ward_server.api.item.service;

import com.ward.ward_server.api.entry.service.EntryService;
import com.ward.ward_server.api.item.dto.ItemResponseDTO;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemService {

    private final EntryService entryService;
    private final ItemRepository itemRepository;

    // 상세 페이지
    public ItemResponseDTO getItemDetail(Long itemId, Long userId) {
        Item item = getItemById(itemId);
        ItemResponseDTO responseDTO = new ItemResponseDTO(item);

        // userId가 넘어오면 응모 여부 확인
        if (userId != null) {
            boolean isEntryExist = entryService.isEntryExist(userId, itemId);
            responseDTO.setIsEntryExist(isEntryExist);
        }

        return responseDTO;
    }

    // ItemId로 상품 1개 조회
    public Item getItemById(Long itemId) {
        // 해당 itemId를 이용하여 Item을 조회
        // TODO 예외 메세지 수정
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + itemId));
    }

}
