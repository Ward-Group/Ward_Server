package com.ward.ward_server.api.item.entity;

import com.ward.ward_server.api.item.entity.enumtype.Category;
import com.ward.ward_server.api.item.entity.enumtype.converter.CategoryConverter;
import com.ward.ward_server.api.wishItem.WishItem;
import com.ward.ward_server.global.Object.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Getter
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String koreanName;

    @Column(nullable = false)
    private String englishName;

    private String mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private Integer viewCount = 0;

    @Column(nullable = false)
    @Convert(converter = CategoryConverter.class)
    private Category category;

    private Integer price;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<WishItem> wishItems = new ArrayList<>();

    @Builder
    public Item(String koreanName, String englishName, String mainImage, String code, Brand brand, Category category, int price) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.mainImage = mainImage;
        this.code = code;
        this.brand = brand;
        this.category = category;
        this.price = price;
    }

    public void increaseViewCount() {
        viewCount += 1;
    }

    public void updateBrand(Brand brand) {
        this.brand = brand;
    }

    public void updateKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public void updateEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void updateMainImage(String mainImage) {
        this.mainImage = mainImage;
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
