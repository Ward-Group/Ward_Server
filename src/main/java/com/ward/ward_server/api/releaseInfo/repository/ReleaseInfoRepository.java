package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReleaseInfoRepository  extends JpaRepository<ReleaseInfo, Long>  {
    List<ReleaseInfo> findAllByItemId(long itemId);
    Optional<ReleaseInfo> findByItemIdAndDrawPlatform(long itemId, String drawPlatform);
    boolean existsByItemIdAndDrawPlatform(long itemId, String drawPlatform);
    void deleteByItemIdAndDrawPlatform(long itemId, String drawPlatform);
}
