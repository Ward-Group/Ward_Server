package com.ward.ward_server.api.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ward.ward_server.api.user.entity.enumtype.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA 쓰면서 protected 키워드는 생성해서 쓰지말라는 의미
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole; //[ROLE_USER,ROLE_ADMIN]

    private String extraInfo;

    //==생성 메서드==//
    public static UserEntity createUser(String email, String password, String extraInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword(password);
        userEntity.setUserRole(UserRole.ROLE_USER);
        userEntity.setExtraInfo(extraInfo);
        return userEntity;
    }
}
