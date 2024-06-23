package com.ward.ward_server.api.entry.entity;

import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_info_id", nullable = false)
    private ReleaseInfo releaseInfo;

    private LocalDateTime entryDate;

    public EntryRecord(User user, ReleaseInfo releaseInfo) {
        this.user = user;
        this.releaseInfo = releaseInfo;
        this.entryDate = LocalDateTime.now();
    }

    public String getEntryDate() {
        return entryDate.format(DATE_STRING_FORMAT);
    }
}