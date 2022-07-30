package com.java.store.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException{
    private final int code;
    private final String message;


    public ServiceException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
