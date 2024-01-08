package ru.clevertec.ecl.web.exc_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.ecl.exception.AppException;
import ru.clevertec.ecl.exception.EntityExistsException;
import ru.clevertec.ecl.exception.NotFoundException;
import ru.clevertec.ecl.exception.UnsupportedOperationException;
import ru.clevertec.ecl.service.dto.error.ErrorDto;

@RequiredArgsConstructor
@RestControllerAdvice("ru.clevertec.ecl")
public class RestExceptionAdvice {

    private static final String MSG_SERVER_ERROR = "Server error";
    private static final String MSG_CLIENT_ERROR = "Client error";
    private static final String DEFAULT_MSG = "Unknown error";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto error(NotFoundException e) {
        return new ErrorDto(MSG_CLIENT_ERROR, e.getMessage(), e.getCode());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDto error(EntityExistsException e) {
        return new ErrorDto(MSG_CLIENT_ERROR, e.getMessage(), e.getCode());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto error(UnsupportedOperationException e) {
        return new ErrorDto(MSG_SERVER_ERROR, e.getMessage(), e.getCode());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto error(AppException e) {
        return new ErrorDto(MSG_SERVER_ERROR, e.getMessage(), e.getCode());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto error(Exception e) {
        return new ErrorDto(MSG_SERVER_ERROR, DEFAULT_MSG, "50000");
    }
}
