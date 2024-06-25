package com.ward.ward_server.api.item.entity;

import com.ward.ward_server.api.item.entity.enums.Category;
import com.ward.ward_server.global.Object.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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

    private String koreanName;

    private String englishName;

    private String mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    private Long viewCount = 0L;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "char(20)")
    private Category category;

    private Integer price;

    @Builder
    public Item(String code, String koreanName, String englishName, String mainImage, Brand brand, Category category, Integer price) {
        this.code = code;
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.mainImage = mainImage;
        this.brand = brand;
        this.category = category;
        this.price = price == null ? 0 : price;
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
