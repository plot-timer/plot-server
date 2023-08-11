package com.plot.plotserver.exception.category;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryAlreadyExistException extends  RuntimeException{

    private String message;

    public CategoryAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}

