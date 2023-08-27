package com.plot.plotserver.dto.request.category;

import lombok.Getter;

import java.util.List;


@Getter
public class UpdateCategoryReqDto {

    private String categoryName;

    private String emoji;

    private boolean star;

    private String tags;

}