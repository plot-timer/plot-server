package com.plot.plotserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.DailyTodo.NewDailyTodoReqDto;
import com.plot.plotserver.dto.request.DailyTodo.SearchDailyTodo;
import com.plot.plotserver.dto.request.DailyTodo.UpdateDailyTodoReqDto;
import com.plot.plotserver.dto.request.record.RecordRequestDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseWithRecordsDto;
import com.plot.plotserver.dto.response.record.RecordResponseDto;
import com.plot.plotserver.service.DailyTodoService;
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
@RequestMapping("api/daily-todos")
public class DailyTodoController {

    private final DailyTodoService dailyTodoService;

    @GetMapping("/{dailyTodoId}") //상세 화면 보여주기.
    @Comment("DailyTodo 상세 화면 보여주기(재생화면)")
    public void showDailyTodo(@PathVariable("dailyTodoId") Long dailyTodoId,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        DailyTodoResponseWithRecordsDto dailyTodoResponseWithRecordsDto = dailyTodoService.searchByDailyTodoId(dailyTodoId);

        Message message = Message.builder()
                .data(dailyTodoResponseWithRecordsDto)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }

    @GetMapping("/date/{date}")
    @Comment("날짜로 DailyTodo 조회하기")
    public void showDailyTodo(@PathVariable("date") String date, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<DailyTodoResponseDto> result = dailyTodoService.searchByDate(date);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }


    @PostMapping("/{todoId}")//todo 저장.  저장 로직 효율적으로 수정하기.
    public void addDailyTodo(@PathVariable("todoId") Long todoId, @RequestBody NewDailyTodoReqDto newDailyTodoReqDto,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());


        DailyTodoResponseDto.Out result = dailyTodoService.save(todoId, newDailyTodoReqDto);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();

        om.writeValue(response.getOutputStream(), message);

    }

    @PatchMapping("/{dailyTodoId}")
    public void updateDailyTodo(@PathVariable("dailyTodoId") Long dailyTodoId, @RequestBody UpdateDailyTodoReqDto updateDailyTodoReqDto, HttpServletResponse response) throws IOException {
        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        dailyTodoService.update(dailyTodoId,updateDailyTodoReqDto);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }

    @DeleteMapping("/{dailyTodoId}")
    public void deleteDailyTodo(HttpServletResponse response,@PathVariable("dailyTodoId") Long dailyTodoId) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        dailyTodoService.delete(dailyTodoId);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


    @GetMapping("/time-sequence/{date}")
    public void getDailyHistoryAndSchedule(@PathVariable("date") String date, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<RecordResponseDto> result = dailyTodoService.getHistoryAndSchedule(date);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


    @GetMapping("/grass/{startDate}/{endDate}")
    public void getHistoryOfMonth(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<RecordResponseDto.Grass> result = dailyTodoService.getHistoryOfMonth(startDate, endDate);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

}
