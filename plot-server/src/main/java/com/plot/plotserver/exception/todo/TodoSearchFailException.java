package com.plot.plotserver.exception.todo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TodoSearchFailException extends  RuntimeException{

    private String message;

    public TodoSearchFailException(String message) {
        super(message);
        this.message = message;
    }
}

