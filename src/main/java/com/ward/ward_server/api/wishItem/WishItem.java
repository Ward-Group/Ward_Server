package com.ward.ward_server.api.wishItem;

import com.ward.ward_server.api.item.entity.Item;
import com.ward.ward_server.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.Object.Constants.DATE_STRING_FORMAT;


@Getter
@Entity
@Table(name = "wish_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "wish_date")
    private LocalDateTime wishDate;

    public WishItem(User user, Item item) {
        this.user = user;
        this.item = item;
        this.wishDate = LocalDateTime.now();
    }

    public String getWishDate(){
        return wishDate.format(DATE_STRING_FORMAT);
    }
}
