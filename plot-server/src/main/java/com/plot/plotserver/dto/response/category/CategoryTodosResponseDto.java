package com.plot.plotserver.dto.response.category;


import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.dto.response.category_group.CategoryGroupResponseDto;
import com.plot.plotserver.dto.response.todo.TodoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class CategoryTodosResponseDto {//카테고리 정보와 그 안의 투두들


    CategoryResponseDto.Sub category;

    List<TodoResponseDto> todo_list;

    public static CategoryTodosResponseDto of(Category category){


        List<Todo> todos = category.getTodos();
        List<TodoResponseDto> temp = new ArrayList<>();

        todos.forEach(todo -> temp.add(TodoResponseDto.of(todo)));

        return CategoryTodosResponseDto.builder()
                .category(CategoryResponseDto.Sub.of(category))
                .todo_list(temp)
                .build();
    }

}


