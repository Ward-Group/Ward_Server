package com.ward.ward_server.api.item.entity;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.item.entity.enumtype.Brand;
import com.ward.ward_server.api.item.entity.enumtype.State;
import com.ward.ward_server.api.item.entity.enumtype.converter.BrandConverter;
import com.ward.ward_server.api.wishlist.domain.Wishlist;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
@Getter
public class Item {
    //TODO ERD 에 맞게 변경하기

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String image;

    @Column
    private String siteUrl;

    @Column(nullable = false)
    private LocalDateTime releaseDate;

    @Column
    private LocalDateTime dueDate;

    @Column
    private LocalDateTime presentationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Column(nullable = false)
    //@Enumerated(EnumType.STRING)
    @Convert(converter = BrandConverter.class)
    //TODO converter로 수정하기
    private Brand brand;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<EntryRecord> entryRecords = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists = new ArrayList<>();

    @Builder
    public Item(String name, String image, String siteUrl, LocalDateTime releaseDate, LocalDateTime dueDate, LocalDateTime presentationDate, State state, Brand brand) {
        this.name = name;
        this.image = image;
        this.siteUrl = siteUrl;
        this.releaseDate = releaseDate;
        this.dueDate = dueDate;
        this.presentationDate = presentationDate;
        this.state = state;
        this.brand = brand;
    }
}
