package com.plot.plotserver.exception.Category;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategorySavedFailException extends RuntimeException {

    private String message;

    public CategorySavedFailException(String message) {
        super(message);
        this.message = message;
    }

}