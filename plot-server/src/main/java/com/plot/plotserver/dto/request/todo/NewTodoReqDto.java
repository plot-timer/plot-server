package com.plot.plotserver.dto.request.todo;


import lombok.Getter;

@Getter
public class NewTodoReqDto {

    private String title;

    private String subtitle;

    private String memo;

    private String emoji;

}
