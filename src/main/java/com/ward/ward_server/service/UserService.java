package com.ward.ward_server.service;

import com.ward.ward_server.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final String EXISTING_EMAIL = "test@test.com";
    private static final String ANOTHER_EMAIL = "next@test.com";

    public Optional<UserEntity> findByEmail(String email) {
        // TODO : Move this to a database
        // 내부로직으로 임시 사용자 만들어서 사용했는데, 이렇게 말고 DB 에서 불러오도록 변경해야함.
        if (EXISTING_EMAIL.equalsIgnoreCase(email)) {
            var user = new UserEntity();
            user.setId(1L);
            user.setEmail(EXISTING_EMAIL);
            user.setPassword("$2a$12$kivpiZrLUW9.44c4P4KpgOrgvH.Y6UkWNP9/nxV5sZW2K5ztE78e6"); // test
            user.setRole("ROLE_ADMIN");
            user.setExtraInfo("My nice admin");
            return Optional.of(user);
        } else if (ANOTHER_EMAIL.equalsIgnoreCase(email)) {
            var user = new UserEntity();
            user.setId(99L);
            user.setEmail(ANOTHER_EMAIL);
            user.setPassword("$2a$12$kivpiZrLUW9.44c4P4KpgOrgvH.Y6UkWNP9/nxV5sZW2K5ztE78e6"); // test
            user.setRole("ROLE_USER");
            user.setExtraInfo("My nice user");
            return Optional.of(user);
        }

        return Optional.empty();
    }
}
