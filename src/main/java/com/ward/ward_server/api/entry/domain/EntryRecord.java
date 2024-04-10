package com.ward.ward_server.api.entry.domain;

import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.webcrawling.entity.Item;
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
    @Column(name = "entry_id")
    private Long entryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "entry_date")
    private Date entryDate;

    public EntryRecord(User user, Item item) {
        this.user = user;
        this.item = item;
        this.entryDate = new Date();
    }
}