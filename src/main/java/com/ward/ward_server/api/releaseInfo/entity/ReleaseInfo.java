package com.ward.ward_server.api.releaseInfo.entity;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ReleaseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    private String drawPlatform;

    private String siteUrl;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    @Column
    private LocalDateTime dueDate;

    @Column
    private LocalDateTime presentationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "releaseInfo", cascade = CascadeType.ALL)
    private List<EntryRecord> entryRecords = new ArrayList<>();

}
