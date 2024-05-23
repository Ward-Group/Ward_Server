package com.ward.ward_server.api.user.entity;

import com.ward.ward_server.api.entry.entity.EntryRecord;
import com.ward.ward_server.api.user.entity.enumtype.Role;
import com.ward.ward_server.api.wishItem.WishItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 쓰면서 protected 키워드는 생성해서 쓰지말라는 의미
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // provider + providerId 정규화

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

    @Column(name = "email_notification", nullable = false)
    private boolean emailNotification;

    @Column(name = "app_push_notification", nullable = false)
    private boolean appPushNotification;

    @Column(name = "sns_notification", nullable = false)
    private boolean snsNotification;

    @Column(name = "refresh_token")
    private String refreshToken; // Refresh Token 필드 추가

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<EntryRecord> entryRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WishItem> wishItems = new ArrayList<>();

    //==생성 메서드==//
    public User(String username, String name, String email, String password, String nickname, Boolean emailNotification, Boolean appPushNotification, Boolean snsNotification) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = Role.ROLE_USER;
        this.password = password;
        this.nickname = nickname;
        this.emailNotification = emailNotification;
        this.appPushNotification = appPushNotification;
        this.snsNotification = snsNotification;
    }

    //==관리자 권한 부여==//
    public void grantAdminRole() {
        this.role = Role.ROLE_ADMIN;
    }

    //==사용자 권한 부여==//
    public void grantUserRole() {
        this.role = Role.ROLE_USER;
    }

    // Refresh Token 갱신 메서드 추가
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
