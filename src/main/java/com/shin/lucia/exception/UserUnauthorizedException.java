package com.shin.lucia.exception;

public class UserUnauthorizedException extends RuntimeException {
    public UserUnauthorizedException(String action) {
        super("Você não tem permissão para " + action);
    }
}
