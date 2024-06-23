package com.ward.ward_server.api.releaseInfo.repository.query;

import com.ward.ward_server.api.releaseInfo.dto.ReleaseInfoSimpleResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReleaseInfoQueryRepository {
    List<ReleaseInfoSimpleResponse> getDueTodayReleaseInfoOrdered(LocalDateTime now);
    List<ReleaseInfoSimpleResponse> getReleaseTodayReleaseInfoOrdered(LocalDateTime now);
    List<ReleaseInfoSimpleResponse> getWishItemReleaseInfoOrdered(long userId, LocalDateTime now);
    List<ReleaseInfoSimpleResponse> getJustConfirmReleaseInfoOrdered(LocalDateTime now);
    List<ReleaseInfoSimpleResponse> getRegisterTodayReleaseInfoOrdered(LocalDateTime now);
}
