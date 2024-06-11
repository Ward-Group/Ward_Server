package com.ward.ward_server.api.item.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String logoImage;

    private String koreanName;

    private String englishName;

    private Long viewCount = 0L;

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
