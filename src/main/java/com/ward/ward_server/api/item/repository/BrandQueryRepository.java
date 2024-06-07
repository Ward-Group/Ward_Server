package com.ward.ward_server.api.item.repository;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;


public interface BrandQueryRepository {
    public Page<BrandInfoResponse> getBrandItemPage(int page, int size);
}
