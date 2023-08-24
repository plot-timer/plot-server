package com.plot.plotserver.service;

import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.request.DailyTodo.NewDailyTodoReqDto;
import com.plot.plotserver.dto.request.DailyTodo.SearchDailyTodo;
import com.plot.plotserver.dto.request.DailyTodo.UpdateDailyTodoReqDto;

import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseDto;
import com.plot.plotserver.exception.dailytodo.DailyTodoAlreadyExistException;
import com.plot.plotserver.exception.dailytodo.DailyTodoDeleteFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoSavedFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoUpdateFailException;
import com.plot.plotserver.repository.DailyTodoRepository;
import com.plot.plotserver.repository.RecordRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.DailyTodoStatusEnum;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyTodoService {


    private final DailyTodoRepository dailyTodoRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    private final RecordRepository recordRepository;


    public List<DailyTodoResponseDto> searchByDate(SearchDailyTodo reqDto){

        Long userId = SecurityContextHolderUtil.getUserId();

        List<DailyTodoResponseDto> result = new ArrayList<>();
        LocalDate date = getLocalDate(reqDto);

        List<DailyTodo> dailyTodos = dailyTodoRepository.findByUserIDAndDate(userId,date);

        for (DailyTodo dailyTodo : dailyTodos) {
            Todo todo = dailyTodo.getTodo();
            Category category = todo.getCategory();
            CategoryGroup categoryGroup = category.getCategoryGroup();

            List<Record> histories= recordRepository.findHistoriesByDailyTodoId(dailyTodo.getId());

            Long total_history=0L;//하루의 dailytotal_history 총 합 구하기.
            Long total_schedule=0L;
            for (Record history : histories) {
                total_history += history.getDuration();
            }

            List<Record> schedules = recordRepository.findSchedulesByDailyTodoId(dailyTodo.getId());
            for (Record schedule : schedules) {
                total_schedule = schedule.getDuration();
            }

            result.add(DailyTodoResponseDto.of(total_history,total_schedule, dailyTodo, todo, category, categoryGroup));
        }
//        dailyTodos.forEach(dailyTodo -> result.add(DailyTodoResponseDto.of(dailyTodo,dailyTodo.getTodo(),dailyTodo.getTodo().getCategory(),dailyTodo.getTodo().getCategory().getCategoryGroup())));

        return result;
    }

    public DailyTodoResponseDto searchByDailyTodoId(Long dailyToId){


        DailyTodo dailyTodo = dailyTodoRepository.findById(dailyToId).get();

        Todo todo = dailyTodo.getTodo();
        Category category = todo.getCategory();
        CategoryGroup categoryGroup = category.getCategoryGroup();

        List<Record> histories= recordRepository.findHistoriesByDailyTodoId(dailyToId);

        Long total_history=0L;//하루의 dailytotal_history 총 합 구하기.
        Long total_schedule=0L;
        for (Record history : histories) {
            total_history += history.getDuration();
        }

        List<Record> schedules = recordRepository.findSchedulesByDailyTodoId(dailyToId);
        for (Record schedule : schedules) {
            total_schedule = schedule.getDuration();
        }

        DailyTodoResponseDto result = DailyTodoResponseDto.of(total_history, total_schedule, dailyTodo, todo, category, categoryGroup);

        return result;
    }

    @Transactional
    public void save(Long todoId,NewDailyTodoReqDto newDailyTodoReqDto) {

        try {

            Long userId = SecurityContextHolderUtil.getUserId();
            User user = userRepository.findById(userId).get();
            Todo todo = todoRepository.findById(todoId).get();

            LocalDate date = getLocalDate(newDailyTodoReqDto);
            Optional<DailyTodo> findDailyTodo = dailyTodoRepository.findByTodoIdAndDailyTodoDate(todo.getId(), date);

            if (findDailyTodo.isPresent()) {
                log.info("dailytodo를 생성할 수 없습니다.");
                throw new DailyTodoAlreadyExistException("원하는 날짜에 해당 Todo로 dailyTodo가 이미 생성되어 있습니다.");
            }

            DailyTodo dailyTodo = DailyTodo.builder()
                    .dailyTodoDate(date)
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

    private static LocalDate getLocalDate(NewDailyTodoReqDto newDailyTodoReqDto) {
        LocalDate date = LocalDate.parse(newDailyTodoReqDto.getDailyTodoDate());
        return date;
    }

    private static LocalDate getLocalDate(SearchDailyTodo searchDailyTodo) {
        LocalDate date = LocalDate.parse(searchDailyTodo.getDailyTodoDate());
        return date;
    }
}
