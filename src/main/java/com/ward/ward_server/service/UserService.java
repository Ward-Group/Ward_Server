package com.ward.ward_server.service;

import com.ward.ward_server.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final String EXISTING_EMAIL = "test@test.com";

    public Optional<UserEntity> findByEmail(String email) {
        // TODO : Move this to a dataabase
        if (! EXISTING_EMAIL.equalsIgnoreCase(email)) return Optional.empty();

        var user = new UserEntity();
        user.setId(1L);
        user.setEmail(EXISTING_EMAIL);
        user.setPassword("$2a$12$kivpiZrLUW9.44c4P4KpgOrgvH.Y6UkWNP9/nxV5sZW2K5ztE78e6"); // test
        user.setRole("ROLE_ADMIN");
        user.setExtraInfo("My nice admin");
        return Optional.of(user);
    }
}
