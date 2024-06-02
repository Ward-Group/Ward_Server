package com.ward.ward_server.api.user.service;

import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public String getNickname(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND));
        return user.getNickname();
    }

    @Transactional
    public void updateNickname(Long userId, String newNickname) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ExceptionCode.USER_NOT_FOUND));

        if (userRepository.existsByNickname(newNickname)) {
            throw new ApiException(ExceptionCode.DUPLICATE_NICKNAME);
        }

        user.setNickname(newNickname);
        userRepository.save(user);
    }
}
