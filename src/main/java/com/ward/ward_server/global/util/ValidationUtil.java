package com.ward.ward_server.global.util;

import org.apache.commons.validator.routines.EmailValidator;

public class ValidationUtil {

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
    private static final String ENGLISH_NAME_REGEX = "^[a-zA-Z0-9 ]+$";


    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean isEnglishName(String name) {
        return name.matches(ENGLISH_NAME_REGEX);
    }

}
