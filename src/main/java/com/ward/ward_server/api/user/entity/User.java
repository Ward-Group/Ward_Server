package com.ward.ward_server.api.user.entity;

import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.user.entity.enumtype.Role;
import com.ward.ward_server.api.wishBrand.WishBrand;
import com.ward.ward_server.api.wishItem.WishItem;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; //[ROLE_USER,ROLE_ADMIN]

    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private boolean emailNotification;

    @Column(nullable = false)
    private boolean appPushNotification;

    @Column(nullable = false)
    private boolean snsNotification;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EntryRecord> entryRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WishItem> wishItems = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<WishBrand> wishBrands = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SocialLogin> socialLogins = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    public User(String name, String email, String password, String nickname, Boolean emailNotification, Boolean appPushNotification, Boolean snsNotification) {
        this.name = name;
        this.email = email;
        this.role = Role.ROLE_USER;
        this.password = password;
        this.nickname = nickname;
        this.emailNotification = emailNotification;
        this.appPushNotification = appPushNotification;
        this.snsNotification = snsNotification;
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changeNickname(String newNickname, boolean isNicknameDuplicate) {
        if (isNicknameDuplicate) {
            throw new ApiException(ExceptionCode.DUPLICATE_NICKNAME);
        }
        updateNickname(newNickname);
    }
}
