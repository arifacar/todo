package com.arifacar.domain.model.constants;

public class ResponseMessages {

    private ResponseMessages() {
        throw new IllegalStateException(ErrorConstants.UTILITY_CLASS);
    }

    public static final String WRONG_USERNAME_OR_PASS = "Username and password did not match.";

}
