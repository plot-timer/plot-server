package com.plot.plotserver.dto.response.category;

import com.plot.plotserver.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CategoryResponseDto {

    private Long category_id;
    private String category_name;
    private String category_group_name;
    private String tagName;
    private boolean star;

    public static CategoryResponseDto of(Category category, String tagName){
        return CategoryResponseDto.builder()
                .category_id(category.getId())
                .category_name(category.getName())
                .category_group_name(category.getCategoryGroup().getName())
                .tagName(tagName)
                .build();
    }

}
