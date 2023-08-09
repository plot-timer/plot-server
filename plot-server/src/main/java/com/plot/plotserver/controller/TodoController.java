
package com.plot.plotserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.plot.plotserver.domain.Message;
import com.plot.plotserver.dto.request.todo.NewTodoReqDto;
import com.plot.plotserver.service.CategoryService;
import com.plot.plotserver.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoManageService;

    private final CategoryService categoryManageService;


    @PostMapping("")//todo 저장.
    public void addTodo(HttpServletResponse response, @RequestBody NewTodoReqDto newTodoReqDto) throws IOException {

        ObjectMapper om = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON.toString());

        todoManageService.save(newTodoReqDto);

        Message message = Message.builder()
                .status(HttpStatus.OK)
                .message("success")
                .build();
        om.writeValue(response.getOutputStream(), message);


    }



}
