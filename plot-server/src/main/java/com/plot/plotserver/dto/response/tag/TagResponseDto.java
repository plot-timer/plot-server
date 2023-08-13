package com.plot.plotserver.dto.response.tag;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.Tag;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TagResponseDto {

    private String tag_name;

    public static TagResponseDto of(String tagName){
        return TagResponseDto.builder()
                .tag_name(tagName)
                .build();
    }
}
