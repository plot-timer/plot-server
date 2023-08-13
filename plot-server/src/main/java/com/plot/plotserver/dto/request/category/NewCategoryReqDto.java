package com.plot.plotserver.dto.request.category;

import lombok.Getter;

import java.util.List;

@Getter
public class NewCategoryReqDto {

    private String categoryName;

    private String emoji;

    private String category_group;

    private String tags;
}
