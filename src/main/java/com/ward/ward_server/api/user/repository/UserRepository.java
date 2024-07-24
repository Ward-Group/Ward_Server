package com.ward.ward_server.api.user.repository;

import com.ward.ward_server.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);
    boolean existsByNickname(String nickname);
    @Query("SELECT u.nickname " +
            "FROM User u " +
            "WHERE u.id = :userId")
    Optional<String> findNicknameById(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user.id = :userId")
    void deleteRefreshTokensByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM WishItem wi WHERE wi.user.id = :userId")
    void deleteWishItemsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM WishBrand wb WHERE wb.user.id = :userId")
    void deleteWishBrandsByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM EntryRecord er WHERE er.user.id = :userId")
    void deleteEntryRecordsByUserId(@Param("userId") Long userId);
}
