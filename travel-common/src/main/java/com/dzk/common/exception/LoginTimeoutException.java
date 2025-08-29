package com.dzk.common.exception;

public class LoginTimeoutException extends RuntimeException {
    public LoginTimeoutException(String message) {
        super(message);
    }
}
