package com.plot.plotserver.dto.request.categorygroup;


import com.plot.plotserver.util.ColorEnum;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UpdateCategoryGroupReqDto {

    private String groupName;

    private ColorEnum color;
}

