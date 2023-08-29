package com.plot.plotserver.dto.request.record;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecordRequestDto {

    private String date;

    @Data
    @NoArgsConstructor
    public static class Grass{
        private String startDate;
        private String endDate;
    }
}
