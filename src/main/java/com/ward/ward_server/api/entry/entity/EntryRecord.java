package com.ward.ward_server.api.entry.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.Object.Constants.DATE_STRING_FORMAT;

@Entity
@Getter
@Table(name = "entry_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long releaseInfoId;

    private LocalDateTime entryDate;

    public EntryRecord(long userId, long releaseInfoId) {
        this.userId = userId;
        this.releaseInfoId = releaseInfoId;
        this.entryDate = LocalDateTime.now();
    }

    public String getEntryDate() {
        return entryDate.format(DATE_STRING_FORMAT);
    }
}