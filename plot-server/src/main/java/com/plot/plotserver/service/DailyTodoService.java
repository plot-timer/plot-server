package com.plot.plotserver.service;

import com.plot.plotserver.domain.*;
import com.plot.plotserver.dto.request.DailyTodo.NewDailyTodoReqDto;
import com.plot.plotserver.dto.request.DailyTodo.SearchDailyTodo;
import com.plot.plotserver.dto.request.DailyTodo.UpdateDailyTodoReqDto;

import com.plot.plotserver.dto.request.record.RecordRequestDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseDto;
import com.plot.plotserver.dto.response.dailyTodo.DailyTodoResponseWithRecordsDto;
import com.plot.plotserver.dto.response.record.RecordResponseDto;
import com.plot.plotserver.exception.dailytodo.DailyTodoAlreadyExistException;
import com.plot.plotserver.exception.dailytodo.DailyTodoDeleteFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoSavedFailException;
import com.plot.plotserver.exception.dailytodo.DailyTodoUpdateFailException;
import com.plot.plotserver.exception.todo.TodoNotHasRecordsOnDateException;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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


    public List<DailyTodoResponseDto> searchByDate(Long userId,String sDate){//최적화 완료


        List<DailyTodoResponseDto> result = new ArrayList<>();

        LocalDate date = LocalDate.parse(sDate);

        List<DailyTodo> dailyTodos = dailyTodoRepository.findByUserIDAndDateJoinTodoAndCategoryAndCategoryGroup(userId,date);//todo,category,categorygroup까지 한꺼번에 fetch join해야 할듯

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

        return result;
    }

    public DailyTodoResponseWithRecordsDto.Sub searchByTodoAndDate(Long todoId,String sDate){//카테고리가 특정 날짜에 가지는  schedule, history기록 줌.  -->Todo 재생화면에서 사용.



        LocalDate date = LocalDate.parse(sDate);

        Optional<DailyTodo> dailyTodo = dailyTodoRepository.findByTodoIdAndDailyTodoDate(todoId, date);

        if (dailyTodo.isEmpty()) {//그 날에 해당 투두로 생성한 기록이 없을때,
            throw new TodoNotHasRecordsOnDateException("해당되는 날짜에 기록이 없습니다.");
        }

        Long total_history=0L;
        Long total_schedule=0L;

        List<RecordResponseDto.InDailyTodo> temp = new ArrayList<>();
        List<Record> histories= recordRepository.findHistoriesByDailyTodoId(dailyTodo.get().getId());

        for (Record history : histories) {
            temp.add(RecordResponseDto.InDailyTodo.of(history));
            total_history += history.getDuration();
        }


        List<Record> schedules = recordRepository.findSchedulesByDailyTodoId(dailyTodo.get().getId());
        for (Record schedule : schedules) {
            temp.add(RecordResponseDto.InDailyTodo.of(schedule));
            total_schedule += schedule.getDuration();
        }

        DailyTodoResponseWithRecordsDto.Sub result = DailyTodoResponseWithRecordsDto.Sub.of(total_history, total_schedule, dailyTodo.get(),temp);

        return result;

    }

    public DailyTodoResponseWithRecordsDto searchByDailyTodoId(Long dailyToId){//최적화 완료


        DailyTodo dailyTodo = dailyTodoRepository.findByIdJoinTodoAndCategoryAndCategoryGroup(dailyToId).get();

        Todo todo = dailyTodo.getTodo();
        Category category = todo.getCategory();
        CategoryGroup categoryGroup = category.getCategoryGroup();


        Long total_history=0L;
        Long total_schedule=0L;

        List<RecordResponseDto.InDailyTodo> temp = new ArrayList<>();
        List<Record> histories= recordRepository.findHistoriesByDailyTodoId(dailyToId);

        for (Record history : histories) {
            temp.add(RecordResponseDto.InDailyTodo.of(history));
            total_history += history.getDuration();
        }


        List<Record> schedules = recordRepository.findSchedulesByDailyTodoId(dailyToId);
        for (Record schedule : schedules) {
            temp.add(RecordResponseDto.InDailyTodo.of(schedule));
            total_schedule += schedule.getDuration();
        }

        DailyTodoResponseWithRecordsDto result = DailyTodoResponseWithRecordsDto.of(total_history, total_schedule, dailyTodo, todo, category, categoryGroup,temp);

        return result;
    }

    @Transactional
    public DailyTodoResponseDto.Out save(Long todoId,NewDailyTodoReqDto newDailyTodoReqDto) {

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

            DailyTodo save = dailyTodoRepository.save(dailyTodo);
            return DailyTodoResponseDto.Out.of(save);

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


    public List<RecordResponseDto> getHistoryAndSchedule(String dateParam) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(dateParam);

        List<DailyTodo> dailyTodos = dailyTodoRepository.findByUserIDAndDate(SecurityContextHolderUtil.getUserId(), date);
        List<RecordResponseDto> result = new ArrayList<>();

        dailyTodos.forEach(dailyTodo -> {
            List<Record> records = recordRepository.findByDailyTodoId(dailyTodo.getId());
            records.forEach(record -> result.add(RecordResponseDto.of(record)));
        });

        return result;
    }

    public List<RecordResponseDto.Grass> getHistoryOfMonth(String startOfDate, String endOfDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startRange = LocalDate.parse(startOfDate, formatter);
        LocalDate endRange = LocalDate.parse(endOfDate, formatter);

        List<RecordResponseDto.Grass> result = new ArrayList<>();

        LocalDate currentDate = startRange;

        while(!currentDate.isAfter(endRange)){

            List<DailyTodo> dailyTodoList = dailyTodoRepository.findByUserIDAndDate(SecurityContextHolderUtil.getUserId(), currentDate);

            dailyTodoList.forEach(dailyTodo -> {
                List<Record> histories = recordRepository.findHistoriesByDailyTodoId(dailyTodo.getId());

                histories.forEach(history -> {

                    LocalDate startDate = history.getStartDate().toLocalDate();
                    LocalDate endDate = history.getEndDate().toLocalDate();
                    LocalDateTime temp;

                    if(endDate.isAfter(startDate)){
                        temp = startDate.atTime(LocalTime.of(23,59,59));
                    }else temp = history.getEndDate();

                    result.add(RecordResponseDto.Grass.of(history, temp));

                });
            });
            currentDate = currentDate.plusDays(1);
        }

        return result;
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
