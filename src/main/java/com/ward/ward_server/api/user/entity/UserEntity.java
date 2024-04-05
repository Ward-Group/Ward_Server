package com.ward.ward_server.api.user.entity;

import com.ward.ward_server.api.entry.domain.EntryRecord;
import com.ward.ward_server.api.user.entity.enumtype.Role;
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
public class UserEntity {
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

    @Column
    private String nickname;

    @Column(name = "email_notification", nullable = false)
    private boolean emailNotification;

    @Column(name = "app_push_notification", nullable = false)
    private boolean appPushNotification;

    @Column(name = "sns_notification", nullable = false)
    private boolean snsNotification;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<EntryRecord> entryRecords = new ArrayList<>();

    //==생성 메서드==//
    public UserEntity(String username, String name, String email, String password, Boolean emailNotification, Boolean appPushNotification, Boolean snsNotification) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = Role.ROLE_USER;
        this.password = password;
        this.nickname = name;
        this.emailNotification = emailNotification;
        this.appPushNotification = appPushNotification;
        this.snsNotification = snsNotification;
    }

    //==생성 메서드 @Setter 사용==//
//    public static UserEntity createUser(String username, String name, String email, String password) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(username);
//        userEntity.setName(name);
//        userEntity.setEmail(email);
//        userEntity.setUserRole(Role.ROLE_USER);
//        userEntity.setPassword(password);
//        return userEntity;
//    }

}
