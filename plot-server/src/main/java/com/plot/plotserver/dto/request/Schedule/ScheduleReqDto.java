package com.plot.plotserver.dto.request.Schedule;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class ScheduleReqDto {

    @Getter
    @Builder
    public static class Create{
        private Long dailyTodoId;
        private String startDate;
        private String endDate;
    }


    @Data
    public static class GetScheduleList{
        private String date;
    }

    @Data
    public static class GetScheduleAndHistory{
        private String date;
    }

    @Data
    public static class Update{
        private Long schedule_id;
        private String startDate;
        private String endDate;
    }

}
