package com.plot.plotserver.dto.response.record;

import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.DailyTodo;
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

    private Long record_id;
    private Long daily_todo_id;
    private boolean is_history;
    private String start_date;
    private String end_date;
    private Long duration;
    private String todo_title;
    private String emoji;
    private String color;


    public static RecordResponseDto of(Record record) {
        DailyTodo dailyTodo = record.getDailyTodo();
        Todo todo = dailyTodo.getTodo();
        CategoryGroup categoryGroup = todo.getCategory().getCategoryGroup();

        return RecordResponseDto.builder()
                .record_id(record.getId())
                .daily_todo_id(dailyTodo.getId())
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
    public static class InDailyTodo {//dailyTodo 상세 화면에 표시되는 record들
        private Long id;
        private String start_date;
        private String end_date;
        private Long duration;
        private boolean is_history;


    public static RecordResponseDto.InDailyTodo of(Record record) {


        return InDailyTodo.builder()
                .id(record.getId())
                .start_date(record.getStartDate().toString())
                .end_date(record.getEndDate().toString())
                .duration(record.getDuration())
                .is_history(record.isHistory())
                .build();
        }
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
