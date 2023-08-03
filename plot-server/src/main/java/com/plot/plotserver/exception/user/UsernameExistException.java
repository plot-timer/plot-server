package com.plot.plotserver.exception.user;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameExistException extends RuntimeException {

    private String message;

    public UsernameExistException(String message) {
        super(message);
        this.message = message;
    }
}
