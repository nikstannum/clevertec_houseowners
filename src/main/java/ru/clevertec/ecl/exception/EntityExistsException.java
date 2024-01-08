package ru.clevertec.ecl.exception;

public class EntityExistsException extends ClientException {
    public EntityExistsException(String message, String code) {
        super(message, code);
    }
}
