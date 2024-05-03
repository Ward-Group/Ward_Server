package com.ward.ward_server.api.releaseInfo.entity;

import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.item.entity.enumtype.Status;
import com.ward.ward_server.global.Object.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ward.ward_server.global.Object.Constants.FORMAT;

@Entity
@Getter
@Table(name = "release_info")
@NoArgsConstructor
public class ReleaseInfo extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "draw_platform_id")
    private DrawPlatform drawPlatform;

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
    public ReleaseInfo(long itemId, DrawPlatform drawPlatform, String siteUrl, LocalDateTime releaseDate, LocalDateTime dueDate, LocalDateTime presentationDate, Status status) {
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

    public void updateDrawPlatform(DrawPlatform drawPlatform) {
        this.drawPlatform = drawPlatform;
    }

    public void updateSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public void updateReleaseDate(String releaseDate) {
        this.releaseDate = LocalDateTime.parse(releaseDate, FORMAT);
    }

    public void updateDueDate(String dueDate) {
        this.dueDate = LocalDateTime.parse(dueDate, FORMAT);
    }

    public void updatePresentationDate(String presentationDate) {
        this.presentationDate = LocalDateTime.parse(presentationDate, FORMAT);
    }

    public String getReleaseDate() {
        return releaseDate.format(FORMAT);
    }

    public String getDueDate() {
        return dueDate.format(FORMAT);
    }

    public String getPresentationDate() {
        return presentationDate.format(FORMAT);
    }

    public LocalDateTime getReleaseLocalDate() {
        return releaseDate;
    }

    public LocalDateTime getDueLocalDate() {
        return dueDate;
    }

    public LocalDateTime getPresentationLocalDate() {
        return presentationDate;
    }
}
