package com.ward.ward_server.api.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchItemsResponse {
    private long totalCount;
    private int totalPages;
    private int currentPage;
    private List<ItemResponse> results;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemResponse {
        private Long id;
        private String mainImage;
        private String koreanName;
        private String englishName;
        private String brandName;
        private int releaseCount;
        private Long viewCount;
    }
}

