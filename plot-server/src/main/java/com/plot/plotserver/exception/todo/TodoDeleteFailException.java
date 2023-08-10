package com.plot.plotserver.exception.todo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TodoDeleteFailException extends RuntimeException{

    private String message;

    public TodoDeleteFailException(String message) {
        super(message);
        this.message = message;
    }
}


