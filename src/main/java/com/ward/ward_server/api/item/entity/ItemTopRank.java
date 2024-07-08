package com.ward.ward_server.api.item.entity;

import com.ward.ward_server.api.item.entity.enums.Category;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class ItemTopRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private int itemRank;

    @Column(nullable = false)
    private LocalDateTime calculatedAt;

    @Builder
    public ItemTopRank(Item item, Category category, int itemRank, LocalDateTime calculatedAt) {
        this.item = item;
        this.category = category;
        this.itemRank = itemRank;
        this.calculatedAt = calculatedAt;
    }
}
