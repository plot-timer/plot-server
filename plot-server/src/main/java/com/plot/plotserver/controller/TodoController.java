
package com.plot.plotserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.todo.NewTodoReqDto;
import com.plot.plotserver.dto.request.todo.UpdateTodoDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseWithRecordsDto;
import com.plot.plotserver.dto.response.todo.TodoResponseDto;
import com.plot.plotserver.dto.response.todo.TodoResponseWithDailyTodoDto;
import com.plot.plotserver.service.DailyTodoService;
import com.plot.plotserver.service.TodoService;
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
@RequestMapping("api/todos")
public class TodoController {

    private final TodoService todoService;

    private final DailyTodoService dailyTodoService;


    @GetMapping("/{todoId}") //상세 화면 보여주기.
    @Comment("Todo 상세 화면 보여주기(재생화면)")
    public void showTodo(@PathVariable Long todoId,@RequestParam("date") String date,HttpServletResponse response) throws IOException {


        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());


        DailyTodoResponseWithRecordsDto.Sub sub = dailyTodoService.searchByTodoAndDate(todoId, date);
        TodoResponseWithDailyTodoDto todoResponseWithDailyTodoDto = todoService.searchByTodoIdJoinHistories(todoId,date,sub);

        Message message = Message.builder()
                .data(todoResponseWithDailyTodoDto)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }

    @PostMapping("/add/{categoryId}")//todo 저장.  저장 로직 효율적으로 수정하기.
    public void addTodo(@PathVariable Long categoryId, @RequestBody NewTodoReqDto newTodoReqDto,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        TodoResponseDto result = todoService.save(categoryId, newTodoReqDto);

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);


    }


    @PatchMapping("/{categoryId}/{todoId}")
    public void updateTodo(@PathVariable Long categoryId, @PathVariable Long todoId, @RequestBody UpdateTodoDto updateTodoDto,HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        todoService.update(categoryId,todoId,updateTodoDto);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);

    }



    @DeleteMapping("/{todoId}")
    public void deleteTodo(HttpServletResponse response,@PathVariable Long todoId) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        todoService.delete(todoId);
        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }



    @GetMapping("/all")
    public void searchAll(HttpServletResponse response) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        List<TodoResponseDto> result = todoService.getAllTodos();

        Message message = Message.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);
    }


}
