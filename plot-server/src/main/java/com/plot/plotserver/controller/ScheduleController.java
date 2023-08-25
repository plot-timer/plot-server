package com.plot.plotserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.Schedule.ScheduleReqDto;
import com.plot.plotserver.dto.response.schedule.ScheduleResponseDto;
import com.plot.plotserver.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("")
    public void addSchedule(@RequestBody List<ScheduleReqDto.Create> reqDtoList, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        scheduleService.save(reqDtoList);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();

        om.writeValue(response.getOutputStream(), message);

    }

    @GetMapping("")
    @Comment("해당 날짜의 스케줄 목록 반환")
    public void showSchedule(@RequestBody ScheduleReqDto.GetScheduleList reqDto, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<ScheduleResponseDto> result = scheduleService.searchByDate(reqDto);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();

        om.writeValue(response.getOutputStream(), message);

    }

    @PatchMapping("/{scheduleId}")
    public void updateSchedule(@PathVariable Long scheduleId, @RequestBody ScheduleReqDto.Update reqDto, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        scheduleService.updateSchedule(scheduleId, reqDto);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();

        om.writeValue(response.getOutputStream(), message);

    }


    @DeleteMapping("/{scheduleId}")
    public void deleteOne(@PathVariable Long scheduleId, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        scheduleService.deleteOne(scheduleId);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();

        om.writeValue(response.getOutputStream(), message);
    }
}
