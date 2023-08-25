package com.plot.plotserver.exception.schedule;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScheduleNotFoundException extends RuntimeException {

    private String message;

    public ScheduleNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
