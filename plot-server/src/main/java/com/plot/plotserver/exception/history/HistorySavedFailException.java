package com.plot.plotserver.exception.history;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HistorySavedFailException extends  RuntimeException{

    private String message;

    public HistorySavedFailException(String message) {
        super(message);
        this.message = message;
    }
}

