package com.plot.plotserver.dto.response.todo;

import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseWithRecordsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class TodoResponseWithDailyTodoDto {

    private Long todo_id;
    private String title;
    private String subtitle;
    private String memo;
    private boolean star;
    private String emoji;
    private Long category_id;
    private String category_name;

    private String category_group_name;
    private String color;

    private DailyTodoResponseWithRecordsDto.Sub dailyTodo;

    public static TodoResponseWithDailyTodoDto of(Todo todo,DailyTodoResponseWithRecordsDto.Sub temp){
        Category category = todo.getCategory();
        CategoryGroup categoryGroup = category.getCategoryGroup();

        return TodoResponseWithDailyTodoDto.builder()
                .todo_id(todo.getId())
                .title(todo.getTitle())
                .subtitle(todo.getSubTitle())
                .memo(todo.getMemo())
                .star(todo.isStar())
                .emoji(todo.getEmoji())
                .category_id(category.getId())
                .category_name(category.getName())
                .category_group_name(categoryGroup.getName())
                .color(categoryGroup.getColor().name())
                .dailyTodo(temp)
                .build();
    }
}


