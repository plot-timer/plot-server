package com.plot.plotserver.service;

import com.plot.plotserver.domain.DailyTodo;
import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.DailyTodo.NewDailyTodoReqDto;
import com.plot.plotserver.dto.request.DailyTodo.UpdateDailyTodoReqDto;
import com.plot.plotserver.exception.dailytodo.DailyTodoAlreadyExistException;
import com.plot.plotserver.exception.dailytodo.DailyTodoDeleteFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoSavedFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoUpdateFailException;
import com.plot.plotserver.repository.DailyTodoRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.DailyTodoStatusEnum;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyTodoService {


    private final DailyTodoRepository dailyTodoRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;


    @Transactional
    public void save(Long todoId,NewDailyTodoReqDto newDailyTodoReqDto) {

        try {

            Long userId = SecurityContextHolderUtil.getUserId();
            User user = userRepository.findById(userId).get();
            Todo todo = todoRepository.findById(todoId).get();

            LocalDateTime dateTime = getLocalDateTime(newDailyTodoReqDto);
            Optional<DailyTodo> findDailyTodo = dailyTodoRepository.findByTodoIdAndDailyTodoDate(todo.getId(), dateTime);

            if (findDailyTodo.isPresent()) {
                log.info("dailytodo를 생성할 수 없습니다.");
                throw new DailyTodoAlreadyExistException("원하는 날짜에 해당 Todo로 dailyTodo가 이미 생성되어 있습니다.");
            }

            DailyTodo dailyTodo = DailyTodo.builder()
                    .dailyTodoDate(dateTime)
                    .done(false)
                    .todo(todo)
                    .status(DailyTodoStatusEnum.EMPTY)
                    .user(user)
                    .build();

            dailyTodoRepository.save(dailyTodo);

        }catch(DailyTodoAlreadyExistException e){
            throw e;
        }
        catch (Exception e) {
            throw new DailyTodoSavedFailException("Daily Todo 생성에 실패하였습니다.");
        }
    }

    @Transactional
    public void update(Long dailyTodoId, UpdateDailyTodoReqDto updateDailyTodoReqDto) {

        try {

            DailyTodo dailyTodo = dailyTodoRepository.findById(dailyTodoId).get();
            dailyTodo.updateDailyTodo(updateDailyTodoReqDto);


        } catch (Exception e){
            throw new DailyTodoUpdateFailException("DailyTodo 수정에 실패했습니다.");
        }
    }


    @Transactional
    public void delete(Long dailyTodoId) {

        try {
            DailyTodo dailyTodo = dailyTodoRepository.findById(dailyTodoId).get();
            dailyTodoRepository.delete(dailyTodo);
        }catch (Exception e){
            throw new DailyTodoDeleteFailException("Daily Todo 삭제에 실패했습니다.");
        }
    }

    private static LocalDateTime getLocalDateTime(NewDailyTodoReqDto newDailyTodoReqDto) {
        LocalDate date = LocalDate.parse(newDailyTodoReqDto.getDailyTodoDate());
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIDNIGHT);//Todo의 시간은 년/월/일까지만 의미가 있어서, 시간은 그냥 자정으로 설정해준다.(시간은 이용 안함.)
        return dateTime;
    }
}
