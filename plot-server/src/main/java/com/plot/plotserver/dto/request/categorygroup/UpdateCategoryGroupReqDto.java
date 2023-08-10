package com.plot.plotserver.dto.request.categorygroup;


import com.plot.plotserver.util.ColorEnum;
import lombok.Getter;


@Getter
public class UpdateCategoryGroupReqDto {

    private String groupName;

    private ColorEnum color;
}

