package com.ward.ward_server.api.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "social_logins")
public class SocialLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false)
    private String providerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public SocialLogin(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    void setUser(User user) {
        this.user = user;
        if (!user.getSocialLogins().contains(this)) {
            user.getSocialLogins().add(this);
        }
    }
}
