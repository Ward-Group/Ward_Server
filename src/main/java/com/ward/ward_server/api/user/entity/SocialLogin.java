package com.ward.ward_server.api.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "social_logins")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_login_id")
    private Long id;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public SocialLogin(String provider, String providerId, String email) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getSocialLogins().contains(this)) {
            user.getSocialLogins().add(this);
        }
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }
}
