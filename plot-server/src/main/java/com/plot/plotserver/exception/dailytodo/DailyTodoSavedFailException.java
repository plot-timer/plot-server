package com.plot.plotserver.exception.dailytodo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DailyTodoSavedFailException extends RuntimeException{

    private String message;

    public DailyTodoSavedFailException(String message) {
        super(message);
        this.message = message;
    }
}


