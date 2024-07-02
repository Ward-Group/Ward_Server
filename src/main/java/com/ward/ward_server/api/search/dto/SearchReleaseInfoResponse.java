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
public class SearchReleaseInfoResponse {
    private long totalCount;
    private int totalPages;
    private int currentPage;
    private List<ReleaseInfoResult> results;

    @Getter
    @AllArgsConstructor
    public static class ReleaseInfoResult {
        private Long id;
        private String mainImage;
        private String platformEnglishName;
        private String koreanName;
        private String releaseDueDate;
        private String releaseMethod;
    }
}
