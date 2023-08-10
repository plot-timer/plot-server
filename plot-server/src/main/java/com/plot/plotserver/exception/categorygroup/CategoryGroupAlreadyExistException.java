package com.plot.plotserver.exception.categorygroup;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryGroupAlreadyExistException extends  RuntimeException{

    private String message;
    public CategoryGroupAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }

}

