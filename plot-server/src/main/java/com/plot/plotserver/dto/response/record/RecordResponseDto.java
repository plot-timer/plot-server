package com.plot.plotserver.dto.response.record;

import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Record;
import com.plot.plotserver.domain.Todo;
import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class RecordResponseDto {

    private Long id;
    private boolean is_history;
    private String start_date;
    private String end_date;
    private Long duration;
    private String todo_title;
    private String emoji;
    private String color;

    public static RecordResponseDto of(Record record){
        Todo todo = record.getDailyTodo().getTodo();
        CategoryGroup categoryGroup = todo.getCategory().getCategoryGroup();

        return RecordResponseDto.builder()
                .id(record.getId())
                .is_history(record.isHistory())
                .start_date(record.getStartDate().toString())
                .end_date(record.getEndDate().toString())
                .duration(record.getDuration())
                .todo_title(todo.getTitle())
                .emoji(todo.getEmoji())
                .color(categoryGroup.getColor().name())
                .build();
    }

    @Data
    @Builder
    public static class Grass{

        private Long todo_id;
        private String color;
        private String date;
        private String start_time;
        private String end_time;
        private Long duration;

        public static RecordResponseDto.Grass of(Record record, LocalDateTime endDateTime){
            Todo todo = record.getDailyTodo().getTodo();
            CategoryGroup categoryGroup = todo.getCategory().getCategoryGroup();

            return RecordResponseDto.Grass.builder()
                    .todo_id(todo.getId())
                    .color(categoryGroup.getColor().name())
                    .date(record.getStartDate().toLocalDate().toString())
                    .start_time(record.getStartDate().toString())
                    .end_time(endDateTime.toString())
                    .duration(Duration.between(record.getStartDate(), endDateTime).getSeconds())
                    .build();
        }
    }

}
