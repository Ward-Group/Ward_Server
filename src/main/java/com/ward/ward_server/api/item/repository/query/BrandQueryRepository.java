package com.ward.ward_server.api.item.repository.query;

import com.ward.ward_server.api.item.dto.BrandInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface BrandQueryRepository {
    Page<BrandInfoResponse> getBrandItemPage(Pageable pageable);
}
