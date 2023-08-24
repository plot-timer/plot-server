package com.plot.plotserver.dto.request.DailyTodo;

import com.plot.plotserver.util.ColorEnum;
import com.plot.plotserver.util.DailyTodoStatusEnum;
import lombok.Getter;

@Getter
public class UpdateDailyTodoReqDto {

    private DailyTodoStatusEnum status;
}
