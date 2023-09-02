package com.plot.plotserver.exception.todo;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TodoNotHasRecordsOnDateException extends RuntimeException{

    private String message;

    public TodoNotHasRecordsOnDateException(String message) {
        super(message);
        this.message = message;
    }
}

