package ru.clevertec.ecl.exception;

public class NotFoundException extends ClientException {
    public NotFoundException(String message, String code) {
        super(message, code);
    }
}
