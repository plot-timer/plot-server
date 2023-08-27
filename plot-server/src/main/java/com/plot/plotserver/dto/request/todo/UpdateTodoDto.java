package com.plot.plotserver.dto.request.todo;

import lombok.Getter;

@Getter
public class UpdateTodoDto {

    private String title;

    private String subtitle;

    private String memo;

    private String emoji;

    private boolean star;

}
