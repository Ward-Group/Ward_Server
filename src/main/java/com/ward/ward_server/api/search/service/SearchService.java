package com.ward.ward_server.api.search.service;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.item.repository.ItemRepository;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.releaseInfo.repository.ReleaseInfoRepository;
import com.ward.ward_server.api.search.dto.IntegratedSearchResponse;
import com.ward.ward_server.api.search.dto.SearchItemsResponse;
import com.ward.ward_server.api.search.dto.SearchReleaseInfoResponse;
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

    private final BrandRepository brandRepository;
    private final ItemRepository itemRepository;
    private final ReleaseInfoRepository releaseInfoRepository;

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

    public SearchReleaseInfoResponse searchReleaseInfos(String keyword, int page, int size) {
        Page<ReleaseInfo> releaseInfos = releaseInfoRepository.searchReleaseInfos(keyword, PageRequest.of(page, size));

        List<SearchReleaseInfoResponse.ReleaseInfoResult> results = releaseInfos.getContent().stream().map(releaseInfo ->
                new SearchReleaseInfoResponse.ReleaseInfoResult(
                        releaseInfo.getId(),
                        releaseInfo.getItem().getMainImage(),
                        releaseInfo.getDrawPlatform().getEnglishName(),
                        releaseInfo.getItem().getKoreanName(),
                        releaseInfo.getDueFormatDate(),
                        releaseInfo.getReleaseMethod().getDesc()
                )
        ).collect(Collectors.toList());

        return new SearchReleaseInfoResponse(
                releaseInfos.getTotalElements(),
                releaseInfos.getTotalPages(),
                releaseInfos.getNumber(),
                results
        );
    }

    public IntegratedSearchResponse integratedSearch(String keyword, int page, int size) {
        // Brand search
        List<Brand> brands = brandRepository.searchBrands(keyword);
        List<IntegratedSearchResponse.BrandResult> brandResults = brands.stream()
                .map(brand -> new IntegratedSearchResponse.BrandResult(
                        brand.getId(),
                        brand.getLogoImage(),
                        brand.getKoreanName(),
                        brand.getEnglishName()))
                .collect(Collectors.toList());

        // Item search
        Page<Item> items = itemRepository.searchItems(keyword, PageRequest.of(page, size));
        List<IntegratedSearchResponse.ItemResult> itemResults = items.getContent().stream()
                .map(item -> {
                    int releaseCount = releaseInfoRepository.countByItemId(item.getId());
                    return new IntegratedSearchResponse.ItemResult(
                            item.getId(),
                            item.getMainImage(),
                            item.getBrand().getKoreanName(),
                            item.getKoreanName(),
                            releaseCount,
                            item.getViewCount());
                })
                .collect(Collectors.toList());
        IntegratedSearchResponse.ItemSearchResult itemSearchResult = new IntegratedSearchResponse.ItemSearchResult(
                items.getTotalElements(),
                itemResults
        );

        // Release info search
        Page<ReleaseInfo> releaseInfos = releaseInfoRepository.searchReleaseInfos(keyword, PageRequest.of(page, size));
        List<IntegratedSearchResponse.ReleaseInfoResult> releaseInfoResults = releaseInfos.getContent().stream()
                .map(releaseInfo -> new IntegratedSearchResponse.ReleaseInfoResult(
                        releaseInfo.getId(),
                        releaseInfo.getItem().getMainImage(),
                        releaseInfo.getDrawPlatform().getEnglishName(),
                        releaseInfo.getItem().getKoreanName(),
                        releaseInfo.getDueFormatDate(),
                        releaseInfo.getReleaseMethod().getDesc()))
                .collect(Collectors.toList());
        IntegratedSearchResponse.ReleaseInfoSearchResult releaseInfoSearchResult = new IntegratedSearchResponse.ReleaseInfoSearchResult(
                releaseInfos.getTotalElements(),
                releaseInfoResults
        );

        return new IntegratedSearchResponse(brandResults, itemSearchResult, releaseInfoSearchResult);
    }
}
