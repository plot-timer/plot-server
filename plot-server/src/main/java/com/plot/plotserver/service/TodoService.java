package com.plot.plotserver.service;


import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.request.todo.NewTodoReqDto;
import com.plot.plotserver.dto.request.todo.UpdateTodoDto;
import com.plot.plotserver.dto.response.category.CategoryTodosResponseDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseDto;
import com.plot.plotserver.dto.response.todo.TodoResponseDto;
import com.plot.plotserver.exception.category.CategoryNotExistException;
import com.plot.plotserver.exception.todo.*;
import com.plot.plotserver.repository.CategoryGroupRepository;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.repository.UserRepository;
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

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryGroupRepository categoryGroupRepository;

    @Transactional
    public void save(NewTodoReqDto todoReqDto) {

        try {
            Long userId = SecurityContextHolderUtil.getUserId();
            Optional<User> user = userRepository.findById(userId);

            //category를 todo의 필드에 저장해야 한다.
            String category_group_and_category = todoReqDto.getCategory_path();

            String[] result=extractBeforeAndAfterSlash(category_group_and_category);
            //result[0]=category_group, result[1]=category이다.

            Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findByUserIdAndName(user.get().getId(), result[0]);

            Optional<Category> category = categoryRepository.findByNameAndCategoryGroupId(result[1], categoryGroup.get().getId());


            Todo todo = Todo.builder()
                    .title(todoReqDto.getTitle())
                    .subTitle(todoReqDto.getSubtitle())
                    .memo(todoReqDto.getMemo())
                    .star(false)
                    .emoji(todoReqDto.getEmoji())
                    .category(category.get())
                    .build();

            todoRepository.save(todo);
        }catch(Exception e){
            throw new TodoSavedFailException("Todo 생성에 실패하였습니다.");
        }
    }

    @Transactional
    public void update(Long todoId, UpdateTodoDto updateTodoDto) {

        try {

            Long userId = SecurityContextHolderUtil.getUserId();
            Optional<User> user = userRepository.findById(userId);

            //category를 todo의 필드에 저장해야 한다.
            String category_group_and_category = updateTodoDto.getCategory_path();

            String[] result=extractBeforeAndAfterSlash(category_group_and_category);
            //result[0]=category_group, result[1]=category이다.

            Optional<CategoryGroup> categoryGroup = categoryGroupRepository.findByUserIdAndName(user.get().getId(), result[0]);

            Category category = categoryRepository.findByNameAndCategoryGroupId(result[1], categoryGroup.get().getId()).get();


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
