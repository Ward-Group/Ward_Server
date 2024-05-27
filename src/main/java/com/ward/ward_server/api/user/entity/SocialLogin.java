package com.ward.ward_server.api.user.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class SocialLogin {
    private String provider;
    private String providerId;

    public SocialLogin(String provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }
}
