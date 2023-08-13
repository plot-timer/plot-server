package com.plot.plotserver.exception.category;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotExistException extends RuntimeException{

    private String message;

    public CategoryNotExistException(String message) {
        super(message);
        this.message = message;
    }
}
