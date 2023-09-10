package com.plot.plotserver.service;


import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.request.todo.NewTodoReqDto;
import com.plot.plotserver.dto.request.todo.UpdateTodoDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseWithRecordsDto;
import com.plot.plotserver.dto.response.todo.TodoResponseDto;
import com.plot.plotserver.dto.response.todo.TodoResponseWithDailyTodoDto;
import com.plot.plotserver.exception.todo.*;
import com.plot.plotserver.repository.CategoryGroupRepository;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.ColorEnum;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoService {

    private final TodoRepository todoRepository;

    private final CategoryRepository categoryRepository;

    private final CategoryGroupRepository categoryGroupRepository;

    private final UserRepository userRepository;

    @Transactional
    public TodoResponseDto save(Long categoryId,NewTodoReqDto todoReqDto) {

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

            Todo savedTodo = todoRepository.save(todo);
            return TodoResponseDto.of(savedTodo);
        }catch(Exception e){
            throw new TodoSavedFailException("Todo 생성에 실패하였습니다.");
        }
    }

    @Transactional
    public TodoResponseDto saveWithOutCategory(NewTodoReqDto todoReqDto) {

        try {
            Long userId = SecurityContextHolderUtil.getUserId();
            User user = userRepository.findById(userId).get();

            Optional<CategoryGroup> categoryGroupOpt = categoryGroupRepository.findByUserIdAndName(userId, "이름없는 그룹");
            CategoryGroup categoryGroup = null;
            Category category = null;

            if(!categoryGroupOpt.isPresent()){
                categoryGroup = CategoryGroup.builder()
                        .name("이름없는 그룹")
                        .user(user)
                        .color(ColorEnum.RED)
                        .build();

                category = Category.builder()
                        .name("이름없는 카테고리")
                        .star(false)
                        .categoryGroup(categoryGroup)
                        .build();

                categoryGroupRepository.save(categoryGroup);
                categoryRepository.save(category);
            }
            else{
                categoryGroup = categoryGroupOpt.get();
                Optional<Category> categoryOpt = categoryRepository.findByNameAndCategoryGroupId("이름없는 카테고리", categoryGroup.getId());
                category = categoryOpt.get();
            }

            Todo todo = Todo.builder()
                    .title(todoReqDto.getTitle())
                    .subTitle(todoReqDto.getSubtitle())
                    .memo(todoReqDto.getMemo())
                    .star(false)
                    .emoji(todoReqDto.getEmoji())
                    .category(category)
                    .build();

            Todo savedTodo = todoRepository.save(todo);
            return TodoResponseDto.of(savedTodo);
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


//    public List<TodoResponseDto> getAllTodos() {
//        List<Todo> todoList = todoRepository.findByUserIdJoinCategoryAndCategoryGroup(SecurityContextHolderUtil.getUserId());
//        List<TodoResponseDto> result = new ArrayList<>();
//
//        todoList.forEach(todo -> {
//            result.add(TodoResponseDto.of(todo));
//        });
//        return result;
//    }

    public TodoResponseWithDailyTodoDto searchByTodoIdJoinHistories(Long todoId, String sDate, DailyTodoResponseWithRecordsDto.Sub temp){//최적화 완료.

        try {
            Todo todo = todoRepository.findByIdJoinCategoryAndCategoryGroup(todoId).get();
            TodoResponseWithDailyTodoDto result = TodoResponseWithDailyTodoDto.of(todo,temp);
            return result;
        } catch (Exception e) {
            throw new TodoNotExistException("Todo를 조회할 수 없습니다.");
        }

    }

}
