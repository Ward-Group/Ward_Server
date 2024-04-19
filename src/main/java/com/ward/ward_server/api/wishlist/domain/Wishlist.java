package com.ward.ward_server.api.wishlist.domain;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Entity
@Table(name = "Wishlist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long wishlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "wish_date")
    private Date wishDate;

    public Wishlist(User user, Item item) {
        this.user = user;
        this.item = item;
        this.wishDate = new Date();
    }
}
