package com.ward.ward_server.global.util;

import com.ward.ward_server.global.exception.ApiException;
import com.ward.ward_server.global.response.error.ErrorCode;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.ArrayList;
import java.util.List;

import static com.ward.ward_server.global.exception.ExceptionCode.INVALID_INPUT;

public class ValidationUtils {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    private static final String ENGLISH_AND_NUMBER_REGEX = "^[a-zA-Z0-9 ]+$"; //영어와 숫자로만 구성됐는지 검사
    private static final String KOREAN_REGEX = ".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"; //한글이 한글자라도 포함되는지 검사


    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean isValidEnglish(String str) {
        return str.matches(ENGLISH_AND_NUMBER_REGEX);
    }

    public static boolean isValidKorean(String str) {
        return str.matches(KOREAN_REGEX);
    }

    public static void validationNames(String koreanName, String englishName) {
        List<String> errorMessages = new ArrayList<>();
        if (koreanName != null && !ValidationUtils.isValidKorean(koreanName))
            errorMessages.add(ErrorCode.INVALID_KOREAN_NAME.getMessage());
        if (englishName != null && !ValidationUtils.isValidEnglish(englishName))
            errorMessages.add(ErrorCode.INVALID_ENGLISH_NAME.getMessage());
        if (!errorMessages.isEmpty()) throw new ApiException(INVALID_INPUT, errorMessages);
    }

}
