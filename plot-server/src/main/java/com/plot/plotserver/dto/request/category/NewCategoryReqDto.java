package com.plot.plotserver.dto.request.category;

import lombok.Getter;

import java.util.List;

@Getter
public class NewCategoryReqDto {

    private String categoryName;

    private String emoji;

    private String categoryGroup;

    private String tags;
}
