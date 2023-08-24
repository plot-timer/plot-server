package com.plot.plotserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.DailyTodo.NewDailyTodoReqDto;
import com.plot.plotserver.dto.request.DailyTodo.SearchDailyTodo;
import com.plot.plotserver.dto.request.DailyTodo.UpdateDailyTodoReqDto;
import com.plot.plotserver.dto.response.category.CategoryResponseDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseDto;
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
    public void showDailyTodo(@PathVariable Long dailyTodoId,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        DailyTodoResponseDto dailyTodoResponseDto = dailyTodoService.searchByDailyTodoId(dailyTodoId);

        Message message = Message.builder()
                .data(dailyTodoResponseDto)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }

    @GetMapping("/date/list")
    @Comment("날짜로 DailyTodo 조회하기")
    public void showDailyTodo(@RequestBody SearchDailyTodo searchDailyTodo, HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<DailyTodoResponseDto> result = dailyTodoService.searchByDate(searchDailyTodo);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }


    @PostMapping("/{todoId}")//todo 저장.  저장 로직 효율적으로 수정하기.
    public void addDailyTodo(@PathVariable Long todoId, @RequestBody NewDailyTodoReqDto newDailyTodoReqDto,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());


        dailyTodoService.save(todoId,newDailyTodoReqDto);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();

        om.writeValue(response.getOutputStream(), message);

    }

    @PatchMapping("/{dailyTodoId}")
    public void updateDailyTodo(@PathVariable Long dailyTodoId, @RequestBody UpdateDailyTodoReqDto updateDailyTodoReqDto, HttpServletResponse response) throws IOException {
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
    public void deleteDailyTodo(HttpServletResponse response,@PathVariable Long dailyTodoId) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        dailyTodoService.delete(dailyTodoId);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }

}
