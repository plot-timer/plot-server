package com.plot.plotserver.dto.response.category_group;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.category.CategoryTodosResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CategoryGroupResponseDto {

    private Long category_group_id;
    private String category_group_name;
    private String color;
    private List<CategoryResponseDto.Sub> category_list;


    public static CategoryGroupResponseDto of(CategoryGroup categoryGroup){
        List<Category> categories = categoryGroup.getCategories();
        List<CategoryResponseDto.Sub> temp = new ArrayList<>();

        categories.forEach(category -> temp.add(CategoryResponseDto.Sub.of(category)));

        return CategoryGroupResponseDto.builder()
                .category_group_id(categoryGroup.getId())
                .category_group_name(categoryGroup.getName())
                .color(categoryGroup.getColor().name())
                .category_list(temp)
                .build();
    }

    @Data
    @Builder
    public static class InTodoAdd {

        private Long category_group_id;
        private String category_group_name;

        private List<CategoryResponseDto.CategoryInfo> categoryList;


        public static CategoryGroupResponseDto.InTodoAdd of(CategoryGroup categoryGroup) {

            List<Category> categories = categoryGroup.getCategories();
            List<CategoryResponseDto.CategoryInfo> temp = new ArrayList<>();

            categories.forEach(category -> temp.add(CategoryResponseDto.CategoryInfo.of(category)));

            return InTodoAdd.builder()
                    .category_group_id(categoryGroup.getId())
                    .category_group_name(categoryGroup.getName())
                    .categoryList(temp)
                    .build();

        }
    }

    @Data
    @Builder
    public static class InDailyTodoAdd {//가지고 있는 카테고리 그룹, 카테고리들, todo들 보여줘야 한다.

        private Long category_group_id;
        private String category_group_name;

        private List<CategoryTodosResponseDto> categoryList;


        public static CategoryGroupResponseDto.InDailyTodoAdd of(CategoryGroup categoryGroup) {

            List<Category> categories = categoryGroup.getCategories();
            List<CategoryTodosResponseDto> temp = new ArrayList<>();

            categories.forEach(category -> temp.add(CategoryTodosResponseDto.of(category)));

            return InDailyTodoAdd.builder()
                    .category_group_id(categoryGroup.getId())
                    .category_group_name(categoryGroup.getName())
                    .categoryList(temp)
                    .build();

        }
    }


    @Data
    @Builder
    public static class InCategoryAdd {

        private Long category_group_id;
        private String category_group_name;

        public static CategoryGroupResponseDto.InCategoryAdd of(CategoryGroup categoryGroup) {

            return InCategoryAdd.builder()
                    .category_group_id(categoryGroup.getId())
                    .category_group_name(categoryGroup.getName())
                    .build();
        }
    }




}
