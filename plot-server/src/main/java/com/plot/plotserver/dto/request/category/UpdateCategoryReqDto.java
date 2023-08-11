package com.plot.plotserver.dto.request.category;

import lombok.Getter;

import java.util.List;


@Getter
public class UpdateCategoryReqDto {

    private String categoryName;

    private String emoji;

    private String category_group;

    private boolean Star;

    private List<String> tags;

}