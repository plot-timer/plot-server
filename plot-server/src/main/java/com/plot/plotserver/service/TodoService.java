package com.plot.plotserver.service;


import com.plot.plotserver.domain.Category;
import com.plot.plotserver.domain.CategoryGroup;
import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.todo.NewTodoReqDto;
import com.plot.plotserver.dto.request.todo.UpdateTodoDto;
import com.plot.plotserver.exception.todo.TodoDeleteFailException;
import com.plot.plotserver.exception.todo.TodoSavedFailException;
import com.plot.plotserver.exception.todo.TodoUpdateFailException;
import com.plot.plotserver.repository.CategoryGroupRepository;
import com.plot.plotserver.repository.CategoryRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.repository.UserRepository;
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
                    .done(false)
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

            Optional<Category> category = categoryRepository.findByNameAndCategoryGroupId(result[1], categoryGroup.get().getId());


            Todo todo = todoRepository.findById(todoId).get();
            todo.setTitle(updateTodoDto.getTitle());
            todo.setSubTitle(updateTodoDto.getSubtitle());
            todo.setMemo(updateTodoDto.getMemo());
            todo.setStar(updateTodoDto.isStar());
            todo.setEmoji(updateTodoDto.getEmoji());
            todo.setDone(updateTodoDto.isDone());
            todo.setCategory(category.get());

            todoRepository.save(todo);

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


}
