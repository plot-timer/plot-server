package com.plot.plotserver.dto.response.category;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.TagCategory;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Data
    @AllArgsConstructor
    @Builder
    public static class Sub{

        private Long category_id;
        private String category_name;
        private String category_group_name;
        private String emoji;
        private List<String> tagName;
        private boolean star;

        public static CategoryResponseDto.Sub of(Category category){

            List<TagCategory> tagCategories = category.getTagCategories();

            Set<String> tagNameSet = new HashSet<>();
            List<String> tagNameList = new ArrayList<>();

            tagCategories.forEach(tagCategory -> tagNameSet.add(tagCategory.getTag().getTagName()));
            tagNameSet.forEach(tagName -> tagNameList.add(tagName));

            return Sub.builder()
                    .category_id(category.getId())
                    .category_name(category.getName())
                    .category_group_name(category.getCategoryGroup().getName())
                    .emoji(category.getEmoji())
                    .tagName(tagNameList)
                    .build();
        }
    }

    @Data
    @Builder
    public static class CategoryInfo{//todo add할때 카테고리 정보

        private Long category_id;
        private String category_name;

        private boolean star;

        public static CategoryResponseDto.CategoryInfo of(Category category) {

            return CategoryInfo.builder()
                    .category_id(category.getId())
                    .category_name(category.getName())
                    .star(category.isStar())
                    .build();
        }

    }


}
