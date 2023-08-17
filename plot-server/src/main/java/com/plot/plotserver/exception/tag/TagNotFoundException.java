package com.plot.plotserver.exception.tag;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TagNotFoundException extends RuntimeException{

    private String message;

    public TagNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
