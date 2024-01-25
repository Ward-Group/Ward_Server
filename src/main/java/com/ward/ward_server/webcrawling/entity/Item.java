package com.ward.ward_server.webcrawling.entity;

import com.ward.ward_server.webcrawling.enumtype.Brand;
import com.ward.ward_server.webcrawling.enumtype.State;
import com.ward.ward_server.webcrawling.enumtype.converter.BrandConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@ToString
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String image;
    @Column(nullable = false)
    private String siteUrl;
    @Column(nullable = false)
    private LocalDateTime releaseDate;
    @Column
    private LocalDateTime dueDate;
    @Column
    private LocalDateTime presentationDate;
    @Column(nullable = false)
    private State state;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    //@Convert(converter = BrandConverter.class)
    //TODO converter로 수정하기
    private Brand brand;

    @Builder
    public Item(String name, String image, String siteUrl, LocalDateTime releaseDate, LocalDateTime dueDate, LocalDateTime presentationDate, State state, Brand brand){
        this.name=name;
        this.image=image;
        this.siteUrl=siteUrl;
        this.releaseDate=releaseDate;
        this.dueDate=dueDate;
        this.presentationDate=presentationDate;
        this.state=state;
        this.brand=brand;
    }
}
