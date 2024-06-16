package com.ward.ward_server.api.item.entity;

import com.ward.ward_server.api.item.entity.enumtype.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemViewCount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Long viewCount;

    @Column(nullable = false)
    private LocalDateTime calculatedAt;

    @Builder
    public ItemViewCount(Category category, Item item, Long viewCount, LocalDateTime calculatedAt) {
        this.category = category;
        this.item = item;
        this.viewCount = viewCount;
        this.calculatedAt = calculatedAt;
    }
}
