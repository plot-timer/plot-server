package com.plot.plotserver.dto.response.dailyTodo;

import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.record.RecordResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DailyTodoResponseWithRecordsDto {

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

    private List<RecordResponseDto.InDailyTodo> record_list;

    @Data
    @Builder
    public static class InSchedule{
        private Long daily_todo_id;
        private String title;
        private String todo_emoji;
        private String color;

    }

    public static DailyTodoResponseWithRecordsDto of(Long total_history, Long total_schedule, DailyTodo dailyTodo, Todo todo, Category category, CategoryGroup categoryGroup,List<RecordResponseDto.InDailyTodo> record_list){


        return DailyTodoResponseWithRecordsDto.builder()
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
                .record_list(record_list)
                .build();
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class Sub {

        private Long daily_todo_id;

        private Long history_sum;

        private Long schedule_sum;

        private List<RecordResponseDto.InDailyTodo> record_list;

        public static DailyTodoResponseWithRecordsDto.Sub of(Long total_history, Long total_schedule, DailyTodo dailyTodo,List<RecordResponseDto.InDailyTodo> record_list){


            return DailyTodoResponseWithRecordsDto.Sub.builder()
                    .daily_todo_id(dailyTodo.getId())
                    .history_sum(total_history)
                    .schedule_sum(total_schedule)
                    .record_list(record_list)
                    .build();
        }

    }


}

