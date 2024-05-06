package com.ward.ward_server.api.item.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Getter
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String logoImage;

    @Column(nullable = false)
    private String name;

    private int viewCount = 0;
    private int wishCount = 0;

    @Builder
    public Brand(String logoImage, String name) {
        this.logoImage = logoImage;
        this.name = name;
    }

    public void updateLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
