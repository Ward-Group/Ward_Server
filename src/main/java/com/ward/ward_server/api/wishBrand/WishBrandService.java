package com.ward.ward_server.api.wishBrand;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class WishBrandService {
    private final WishBrandRepository wishBrandRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    public void createWishBrand(long userId, String brandName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (wishBrandRepository.existsByUserIdAndBrandId(userId, brand.getId()))
            throw new ApiException(DUPLICATE_WISH_BRAND);
        wishBrandRepository.save(new WishBrand(user, brand));
    }

    @Transactional(readOnly = true)
    public PageResponse<WishBrandResponse> getWishBrandListByUser(int page, int size, long userId) {
        Page<WishBrand> wishBrandPage = wishBrandRepository.findAllByUserId(userId, PageRequest.of(page, size));
        List<WishBrand> contents = wishBrandPage.getContent();
        List<WishBrandResponse> responses = contents.stream()
                .map(wb -> new WishBrandResponse(
                        wb.getBrand().getLogoImage(),
                        wb.getBrand().getName(),
                        wb.getBrand().getWishCount()))
                .toList();
        return new PageResponse<>(responses, wishBrandPage);
    }

    @Transactional
    public void deleteWishBrand(long userId, String brandName) {
        Brand brand = brandRepository.findByName(brandName).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (!wishBrandRepository.existsByUserIdAndBrandId(userId, brand.getId()))
            throw new ApiException(WISH_BRAND_NOT_FOUND);
        wishBrandRepository.deleteByUserIdAndBrandId(userId, brand.getId());
    }
}
