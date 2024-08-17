package com.ward.ward_server.api.wishBrand;

import com.ward.ward_server.api.item.entity.Brand;
import com.ward.ward_server.api.item.repository.BrandRepository;
import com.ward.ward_server.api.user.entity.User;
import com.ward.ward_server.api.user.repository.UserRepository;
import com.ward.ward_server.api.wishBrand.repository.WishBrandRepository;
import com.ward.ward_server.global.Object.PageResponse;
import com.ward.ward_server.global.Object.enums.BasicSort;
import com.ward.ward_server.global.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ward.ward_server.global.Object.Constants.API_PAGE_SIZE;
import static com.ward.ward_server.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WishBrandService {
    private final WishBrandRepository wishBrandRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    public void createWishBrand(long userId, long brandId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new ApiException(BRAND_NOT_FOUND));
        if (wishBrandRepository.existsByUserIdAndBrandId(userId, brand.getId())) {
            throw new ApiException(DUPLICATE_WISH_BRAND);
        }
        wishBrandRepository.save(new WishBrand(user, brand));
    }

    public PageResponse<WishBrandResponse> getWishBrandListByUser(long userId, BasicSort basicSort, int page) {
        Page<WishBrandResponse> wishBrandPage = wishBrandRepository.getWishBrandPage(userId, basicSort, PageRequest.of(page, API_PAGE_SIZE));
        return new PageResponse<>(wishBrandPage.getContent(), wishBrandPage);
    }

    @Transactional
    public void deleteWishBrand(long userId, long brandId) {
        if (!wishBrandRepository.existsByUserIdAndBrandId(userId, brandId)) {
            throw new ApiException(WISH_BRAND_NOT_FOUND);
        }
        wishBrandRepository.deleteByUserIdAndBrandId(userId, brandId);
    }
}
