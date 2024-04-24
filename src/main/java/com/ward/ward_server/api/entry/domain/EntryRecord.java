package com.ward.ward_server.api.entry.domain;

import com.ward.ward_server.api.releaseInfo.entity.ReleaseInfo;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.item.entity.Item;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Table(name = "EntryRecord")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 쓰면서 protected 키워드는 생성해서 쓰지말라는 의미
public class EntryRecord {
    //응모 내역

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "releaseInfo_id")
    private ReleaseInfo releaseInfo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_date")
    private Date entryDate;

    private String memo;

    public EntryRecord(User user, ReleaseInfo releaseInfo) {
        this.user = user;
        this.releaseInfo = releaseInfo;
        this.entryDate = new Date();
    }
}