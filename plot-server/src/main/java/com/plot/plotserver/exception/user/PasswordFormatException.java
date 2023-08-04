package com.plot.plotserver.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PasswordFormatException extends RuntimeException {

    private String message;
    public PasswordFormatException(String message) {
        super(message);
        this.message = message;
    }
}