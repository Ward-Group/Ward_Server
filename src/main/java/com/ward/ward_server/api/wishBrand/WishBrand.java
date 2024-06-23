package com.ward.ward_server.api.wishBrand;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ward.ward_server.global.Object.Constants.DATE_STRING_FORMAT;


@Getter
@Entity
@Table(name = "wish_brand")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "wish_date")
    private LocalDateTime wishDate;

    public WishBrand(User user, Brand brand) {
        this.user = user;
        this.brand = brand;
        this.wishDate = LocalDateTime.now();
    }

    public String getWishDate() {
        return wishDate.format(DATE_STRING_FORMAT);
    }
}
