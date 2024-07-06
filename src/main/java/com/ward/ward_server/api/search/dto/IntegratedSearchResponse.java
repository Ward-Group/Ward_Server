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
public class IntegratedSearchResponse {
    private List<BrandResult> brands;
    private ItemSearchResult items;
    private ReleaseInfoSearchResult releaseInfos;

    @Getter
    @AllArgsConstructor
    public static class BrandResult {
        private Long id;
        private String logoImage;
        private String koreanName;
        private String englishName;
    }

    @Getter
    @AllArgsConstructor
    public static class ItemResult {
        private Long id;
        private String mainImage;
        private String brandName;
        private String koreanName;
        private int releaseCount;
        private long viewCount;
    }

    @Getter
    @AllArgsConstructor
    public static class ItemSearchResult {
        private long totalCount;
        private List<ItemResult> results;
    }

    @Getter
    @AllArgsConstructor
    public static class ReleaseInfoResult {
        private Long id;
        private String mainImage;
        private String platformName;
        private String koreanName;
        private String releaseEndDate;
        private String releaseMethod;
    }

    @Getter
    @AllArgsConstructor
    public static class ReleaseInfoSearchResult {
        private long totalCount;
        private List<ReleaseInfoResult> results;
    }
}
