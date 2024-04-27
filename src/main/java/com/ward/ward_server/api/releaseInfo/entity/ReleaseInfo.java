package com.ward.ward_server.api.releaseInfo.entity;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "release_info")
@NoArgsConstructor
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

    @Builder
    public ReleaseInfo(long itemId, String drawPlatform, String siteUrl, LocalDateTime releaseDate, LocalDateTime dueDate, LocalDateTime presentationDate, Status status) {
        this.itemId = itemId;
        this.drawPlatform = drawPlatform;
        this.siteUrl = siteUrl;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
        this.presentationDate = presentationDate;
        this.status = status;
    }

    public void updateItemId(long itemId) {
        this.itemId = itemId;
    }

    public void updateDrawPlatform(String drawPlatform) {
        this.drawPlatform = drawPlatform;
    }

    public void updateSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void updateReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void updateDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void updatePresentationDate(LocalDateTime presentationDate) {
        this.presentationDate = presentationDate;
    }
}
