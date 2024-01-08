package ru.clevertec.ecl.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final String code;

    public AppException(String message, String code) {
        super(message);
        this.code = code;
    }
}
