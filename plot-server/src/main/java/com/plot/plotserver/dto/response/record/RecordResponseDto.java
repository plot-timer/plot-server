package com.plot.plotserver.dto.response.record;

import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Record;
import com.plot.plotserver.domain.Todo;
import lombok.Builder;
import lombok.Data;

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

}
