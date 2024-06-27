package com.ward.ward_server.api.search.service;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.api.search.dto.SearchItemsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchService {

    private final ItemRepository itemRepository;
    private final ReleaseInfoRepository releaseInfoRepository;

    @Transactional
    public SearchItemsResponse searchItems(String keyword, int page, int size) {
        Page<Item> items = itemRepository.searchItems(keyword, PageRequest.of(page, size));

        List<SearchItemsResponse.ItemResponse> results = items.getContent().stream().map(item -> {
            int releaseCount = releaseInfoRepository.countByItemId(item.getId());
            return new SearchItemsResponse.ItemResponse(
                    item.getId(),
                    item.getMainImage(),
                    item.getKoreanName(),
                    item.getEnglishName(),
                    item.getBrand().getKoreanName(),
                    releaseCount,
                    item.getViewCount()
            );
        }).collect(Collectors.toList());

        return new SearchItemsResponse(
                items.getTotalElements(),
                items.getTotalPages(),
                items.getNumber(),
                results
        );
    }
}
