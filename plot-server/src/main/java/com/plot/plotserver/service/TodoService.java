package com.plot.plotserver.service;


import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.request.todo.NewTodoReqDto;
import com.plot.plotserver.dto.request.todo.UpdateTodoDto;
import com.plot.plotserver.dto.response.todo.TodoResponseDto;
import com.plot.plotserver.exception.todo.*;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(Long categoryId,NewTodoReqDto todoReqDto) {

        try {

            Category category = categoryRepository.findById(categoryId).get();

            Todo todo = Todo.builder()
                    .title(todoReqDto.getTitle())
                    .subTitle(todoReqDto.getSubtitle())
                    .memo(todoReqDto.getMemo())
                    .star(false)
                    .emoji(todoReqDto.getEmoji())
                    .category(category)
                    .build();

            todoRepository.save(todo);
        }catch(Exception e){
            throw new TodoSavedFailException("Todo 생성에 실패하였습니다.");
        }
    }

    @Transactional
    public void update(Long categoryId, Long todoId, UpdateTodoDto updateTodoDto) {

        try {

            Category category = categoryRepository.findById(categoryId).get();
            Todo todo = todoRepository.findById(todoId).get();
            todo.updateTodo(updateTodoDto, category);

        }catch (Exception e){
            throw new TodoUpdateFailException("Todo 수정에 실패했습니다.");
        }
    }


    @Transactional
    public void delete(Long todoId) {

        try {
            Optional<Todo> todo = todoRepository.findById(todoId);
            todoRepository.delete(todo.get());
        }catch (Exception e){
            throw new TodoDeleteFailException("Todo 삭제에 실패했습니다.");
        }
    }

    public static String[] extractBeforeAndAfterSlash(String input) {
        String[] result = new String[2];
        int index = input.indexOf('/');

        if (index >= 0) {
            result[0] = input.substring(0, index);
            result[1] = input.substring(index + 1);
        } else {
            result[0] = input;
            result[1] = "";
        }

        return result;
    }


    public List<TodoResponseDto> getAllTodos() {
        List<Todo> todoList = todoRepository.findByUserId(SecurityContextHolderUtil.getUserId());
        List<TodoResponseDto> result = new ArrayList<>();

        todoList.forEach(todo -> {
            result.add(TodoResponseDto.of(todo));
        });
        return result;
    }

    public TodoResponseDto searchByTodoId(Long todoId){

        try {
            Todo todo = todoRepository.findById(todoId).get();
            TodoResponseDto result = TodoResponseDto.of(todo);
            return result;
        } catch (Exception e) {
            throw new TodoNotExistException("Todo를 조회할 수 없습니다.");
        }

    }

}
