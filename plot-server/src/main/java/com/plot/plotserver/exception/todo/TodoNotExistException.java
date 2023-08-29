package com.plot.plotserver.exception.todo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotExistException extends  RuntimeException{

    private String message;

    public TodoNotExistException(String message) {
        super(message);
        this.message = message;
    }
}



