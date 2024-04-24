package com.ward.ward_server.api.item.entity;

import com.ward.ward_server.api.item.entity.enumtype.Brand;
import com.ward.ward_server.api.item.entity.enumtype.Category;
import com.ward.ward_server.api.item.entity.enumtype.converter.BrandConverter;
import com.ward.ward_server.api.item.entity.enumtype.converter.CategoryConverter;
import com.ward.ward_server.api.wishlist.domain.Wishlist;
import com.ward.ward_server.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
@Getter
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemImage> itemImages = new ArrayList<>();

    @Column(nullable = false)
    @Convert(converter = BrandConverter.class)
    private Brand brand;

    private Long viewCount = 0L;

    @Column(nullable = false)
    @Convert(converter = CategoryConverter.class)
    private Category category;

    private int price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists = new ArrayList<>();

    @Builder
    public Item(String name, String code, Brand brand, Category category, int price) {
        this.name = name;
        this.code = code;
        this.brand = brand;
        this.category = category;
        this.price = price;
    }

    public void addItemImages(List<ItemImage> itemImages) {
        this.itemImages = itemImages;
    }

    public void increaseViewCount() {
        viewCount += 1;
    }

    public void updateBrand(Brand brand) {
        this.brand = brand;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateCode(String code) {
        this.code = code;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updatePrice(int price) {
        this.price = price;
    }
}
