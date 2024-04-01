package com.ward.ward_server.api.user.entity;

import com.ward.ward_server.api.user.entity.enumtype.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 쓰면서 protected 키워드는 생성해서 쓰지말라는 의미
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String username; // provider + providerId 정규화
    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole; //[ROLE_USER,ROLE_ADMIN]

    private String password;

    private String extraInfo;

    //==생성 메서드==//
//    public static UserEntity createUser(String username, String name, String email, String password) {
//        UserEntity userEntity = new UserEntity();
//        userEntity.setUsername(username);
//        userEntity.setName(name);
//        userEntity.setEmail(email);
//        userEntity.setUserRole(UserRole.ROLE_USER);
//        userEntity.setPassword(password);
//        return userEntity;
//    }

    //==생성 메서드==//
    public UserEntity(String username, String name, String email, String password) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.userRole = UserRole.ROLE_USER;
        this.password = password;
    }
}
