package com.ward.ward_server.api.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false, length = 1000)
    private String providerId;

    @Column
    private String email;

    @Column
    private String appleRefreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public SocialLogin(String provider, String providerId, String email, String appleRefreshToken) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.appleRefreshToken = appleRefreshToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }
}
