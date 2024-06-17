package com.ward.ward_server.api.releaseInfo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "draw_platform")
@NoArgsConstructor
public class DrawPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String koreanName;
    private String englishName;
    private String logoImage;

    @Builder
    public DrawPlatform(String koreanName, String englishName, String logoImage) {
        this.koreanName = koreanName;
        this.englishName = englishName;
        this.logoImage = logoImage;
    }

    public void updateKoreanName(String koreanName) {
        this.koreanName = koreanName;
    }

    public void updateEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public void updateLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

}
