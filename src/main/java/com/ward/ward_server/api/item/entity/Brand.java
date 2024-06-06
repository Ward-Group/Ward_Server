package com.ward.ward_server.api.item.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logoImage;

    @Column(nullable = false)
    private String koreanName;

    @Column(nullable = false)
    private String englishName;

    private Integer viewCount = 0;

    @Builder
    public Brand(String logoImage, String koreanName, String englishName) {
        this.logoImage = logoImage;
        this.koreanName = koreanName;
        this.englishName = englishName;
    }

    public void updateLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public void updateKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public void updateEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void increaseViewCount() {
        viewCount += 1;
    }
}
