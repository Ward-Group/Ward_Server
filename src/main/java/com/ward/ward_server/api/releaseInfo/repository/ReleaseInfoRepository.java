package com.ward.ward_server.api.releaseInfo.repository;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseInfoRepository  extends JpaRepository<ReleaseInfo, Long>  {
}
