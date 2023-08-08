
package com.plot.plotserver.controller;


import com.plot.plotserver.dto.request.todo.NewTodoReqDto;
import com.plot.plotserver.service.TodoManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoManageService todoManageService;



    @PostMapping("")//todo 저장.
    public void addTodo(@RequestBody NewTodoReqDto newTodoReqDto) {

    }



}
