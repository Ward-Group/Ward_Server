package com.ward.ward_server.api.entry.entity;

import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

import static com.ward.ward_server.global.Object.Constants.FORMAT;

@Entity
@Getter
@Table(name = "entry_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_info_id")
    private ReleaseInfo releaseInfo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_date")
    private LocalDateTime entryDate;

    private String memo;

    public EntryRecord(User user, ReleaseInfo releaseInfo, String memo) {
        this.user = user;
        this.releaseInfo = releaseInfo;
        this.entryDate = LocalDateTime.now();
        this.memo = memo;
    }

    public String getEntryDate(){
        return entryDate.format(FORMAT);
    }
}