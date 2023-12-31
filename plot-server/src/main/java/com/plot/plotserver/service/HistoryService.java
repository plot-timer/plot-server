package com.plot.plotserver.service;

import com.plot.plotserver.domain.DailyTodo;
import com.plot.plotserver.domain.Record;
import com.plot.plotserver.domain.Todo;
import com.plot.plotserver.domain.User;
import com.plot.plotserver.dto.request.history.HistoryReqDto;
import com.plot.plotserver.exception.history.HistorySavedFailException;
import com.plot.plotserver.repository.DailyTodoRepository;
import com.plot.plotserver.repository.RecordRepository;
import com.plot.plotserver.repository.TodoRepository;
import com.plot.plotserver.repository.UserRepository;
import com.plot.plotserver.util.DailyTodoStatusEnum;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final UserRepository userRepository;

    private final RecordRepository recordRepository;

    private final DailyTodoRepository dailyTodoRepository;

    private final TodoRepository todoRepository;

    @Transactional
    public void saveDailyTodoHistory(Long dailyTodoId, HistoryReqDto historyReqDto) {

        try {

            DailyTodo foundDailyTodo = dailyTodoRepository.findById(dailyTodoId).get();
            Todo todo = foundDailyTodo.getTodo();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(historyReqDto.getStartDate(), formatter); // 시작 날짜 및 시간을 LocalDateTime으로 변환
            LocalDateTime endDateTime = LocalDateTime.parse(historyReqDto.getEndDate(), formatter); // 종료 날짜 및 시간을 LocalDateTime으로 변환

            LocalDate startDate = startDateTime.toLocalDate();
            LocalTime startTime = startDateTime.toLocalTime();

            LocalDate endDate = endDateTime.toLocalDate();
            LocalTime endTime = endDateTime.toLocalTime();


            while (!startDate.isAfter(endDate)) {
                LocalDateTime startDateWithTime = startDate.atTime(startTime);
                LocalDateTime endDateWithTime;

                if (startDate.isEqual(endDate)) {   // 시작 날짜 == 끝 날짜
                    endDateWithTime = endDate.atTime(endTime);
                } else {    // 시작 날짜 < 끝 날짜
                    endDateWithTime = startDate.atTime(LocalTime.of(23,59,59));//23:59:59로 설정.
                }


                // 해당 날짜에 이미 같은 투두의, daily-todo가 존재하는지 확인
                Optional<DailyTodo> dailyTodoOpt = dailyTodoRepository.findByTodoIdAndDailyTodoDate(todo.getId(), startDate);

                DailyTodo dailyTodoToSave;

                if(!dailyTodoOpt.isPresent()){  // 존재하지 않는다면, 새로 생성해준다.
                    DailyTodo dailyTodo = DailyTodo.builder()
                            .dailyTodoDate(startDate)
                            .status(DailyTodoStatusEnum.EMPTY)
                            .todo(todo)
                            .user(foundDailyTodo.getUser())
                            .build();

                    dailyTodoToSave = dailyTodoRepository.save(dailyTodo);
                }
                else    // 이미 해당 날짜에 데일리 투두가 존재
                    dailyTodoToSave = dailyTodoOpt.get();

                Duration duration = Duration.between(startDateWithTime, endDateWithTime);
                long totalSeconds = duration.getSeconds();

                Record record = Record.builder()
                        .isHistory(true)
                        .startDate(startDateWithTime)
                        .endDate(endDateWithTime)
                        .duration(totalSeconds)
                        .dailyTodo(dailyTodoToSave)
                        .build();

                recordRepository.save(record);

                startDate = startDate.plusDays(1);
                startTime = LocalTime.MIN;//00:00:00으로 설정.

            }
        }catch(Exception e){
            throw new HistorySavedFailException("History 생성에 실패하였습니다.");
        }
    }

    @Transactional
    public void saveTodoHistory(Long todoId, HistoryReqDto historyReqDto) {

        //history 기록이 있으면, 기록이 포함된 날짜에 dailytodo가 todoid로 되어 있는게 있는지 확인하고, 있으면 그 dailytodo로 history만들고, 아니면 새로운 dailytodo만들고, 그것으로 history만들어야 한다.
        //시작 날짜, 끝날짜 까지 쭉.

        try {


            Long userId = SecurityContextHolderUtil.getUserId();
            User user = userRepository.findById(userId).get();

            Todo findTodo = todoRepository.findById(todoId).get();


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(historyReqDto.getStartDate(), formatter); // 시작 날짜 및 시간을 LocalDateTime으로 변환
            LocalDateTime endDateTime = LocalDateTime.parse(historyReqDto.getEndDate(), formatter); // 종료 날짜 및 시간을 LocalDateTime으로 변환

            LocalDate startDate = startDateTime.toLocalDate();
            LocalTime startTime = startDateTime.toLocalTime();

            LocalDate endDate = endDateTime.toLocalDate();
            LocalTime endTime = endDateTime.toLocalTime();


            while (!startDate.isAfter(endDate)) {
                LocalDateTime startDateWithTime = startDate.atTime(startTime);
                LocalDateTime endDateWithTime;

                if (startDate.isEqual(endDate)) {   // 시작 날짜 == 끝 날짜
                    endDateWithTime = endDate.atTime(endTime);
                } else {    // 시작 날짜 < 끝 날짜
                    endDateWithTime = startDate.atTime(LocalTime.of(23,59,59));//23:59:59로 설정.
                }

                // 해당 날짜에 이미 같은 투두의, daily-todo가 존재하는지 확인
                Optional<DailyTodo> dailyTodoOpt = dailyTodoRepository.findByTodoIdAndDailyTodoDate(findTodo.getId(), startDate);

                DailyTodo dailyTodoToSave;

                if(!dailyTodoOpt.isPresent()){  // 존재하지 않는다면, 새로 생성해준다.
                    DailyTodo dailyTodo = DailyTodo.builder()
                            .dailyTodoDate(startDate)
                            .status(DailyTodoStatusEnum.EMPTY)
                            .todo(findTodo)
                            .user(user)
                            .build();

                    dailyTodoToSave = dailyTodoRepository.save(dailyTodo);
                }
                else    // 이미 해당 날짜에 데일리 투두가 존재
                    dailyTodoToSave = dailyTodoOpt.get();

                Duration duration = Duration.between(startDateWithTime, endDateWithTime);
                long totalSeconds = duration.getSeconds();

                Record record = Record.builder()
                        .isHistory(true)
                        .startDate(startDateWithTime)
                        .endDate(endDateWithTime)
                        .duration(totalSeconds)
                        .dailyTodo(dailyTodoToSave)
                        .build();

                recordRepository.save(record);

                startDate = startDate.plusDays(1);
                startTime = LocalTime.MIN;//00:00:00으로 설정.

            }
        }catch(Exception e){
            throw new HistorySavedFailException("History 생성에 실패하였습니다.");
        }
    }
}
