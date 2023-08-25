package com.plot.plotserver.dto.response.schedule;

import com.plot.plotserver.domain.DailyTodo;
import com.plot.plotserver.domain.Record;
import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseDto;
import com.plot.plotserver.util.ColorEnum;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class ScheduleResponseDto {

    private Long id;
    private String start_date;
    private String end_date;
    private Long duration;

    private DailyTodoResponseDto.InSchedule daily_todo;

    public static ScheduleResponseDto of(Record schedule){
        DailyTodo dailyTodo = schedule.getDailyTodo();
        Todo todo = dailyTodo.getTodo();
        ColorEnum color = todo.getCategory().getCategoryGroup().getColor();

        return ScheduleResponseDto.builder()
                .id(schedule.getId())
                .start_date(schedule.getStartDate().toString())
                .end_date(schedule.getEndDate().toString())
                .duration(schedule.getDuration())
                .daily_todo(
                        DailyTodoResponseDto.InSchedule.builder()
                                .daily_todo_id(dailyTodo.getId())
                                .title(todo.getTitle())
                                .color(color.name())
                                .todo_emoji(todo.getEmoji())
                                .build()
                ).build();
    }

}
