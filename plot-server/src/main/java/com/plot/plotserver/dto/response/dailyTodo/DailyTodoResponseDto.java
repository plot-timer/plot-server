package com.plot.plotserver.dto.response.dailyTodo;

import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class DailyTodoResponseDto {

    private Long daily_todo_id;
    private String title;

    private String subtitle;

    private String memo;

    private boolean star;

    private String todo_emoji;

    private String category_emoji;

    private String category_name;

    private String color;

    private String status;

    private Long history_sum;

    private Long schedule_sum;

    public static DailyTodoResponseDto of(Long total_history,Long total_schedule,DailyTodo dailyTodo, Todo todo, Category category, CategoryGroup categoryGroup){
        return DailyTodoResponseDto.builder()
                .daily_todo_id(dailyTodo.getId())
                .title(todo.getTitle())
                .subtitle(todo.getSubTitle())
                .memo(todo.getMemo())
                .star(todo.isStar())
                .todo_emoji(todo.getEmoji())
                .category_emoji(category.getEmoji())
                .category_name(category.getName())
                .color(categoryGroup.getColor().name())
                .status(dailyTodo.getStatus().name())
                .history_sum(total_history)
                .schedule_sum(total_schedule)
                .build();
    }


}

