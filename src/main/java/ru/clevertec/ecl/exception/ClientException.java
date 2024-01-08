package ru.clevertec.ecl.exception;

public class ClientException extends AppException {
    public ClientException(String message, String code) {
        super(message, code);
    }
}
