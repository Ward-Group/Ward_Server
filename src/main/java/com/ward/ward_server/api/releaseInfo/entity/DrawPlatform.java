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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String logoImage;

    @Builder
    public DrawPlatform(String name, String logoImage) {
        this.name = name;
        this.logoImage = logoImage;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }
}
