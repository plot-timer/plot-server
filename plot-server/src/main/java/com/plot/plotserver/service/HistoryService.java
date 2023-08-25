package com.plot.plotserver.service;

import com.plot.plotserver.domain.DailyTodo;
import com.plot.plotserver.domain.Record;
import com.plot.plotserver.dto.request.history.HistoryReqDto;
import com.plot.plotserver.exception.history.HistorySavedFailException;
import com.plot.plotserver.repository.DailyTodoRepository;
import com.plot.plotserver.repository.RecordRepository;
import com.plot.plotserver.util.DailyTodoStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final RecordRepository recordRepository;

    private final DailyTodoRepository dailyTodoRepository;

    @Transactional
    public void save(Long dailyTodoId, HistoryReqDto historyReqDto) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(historyReqDto.getStartDate(), formatter); // 시작 날짜 및 시간을 LocalDateTime으로 변환
            LocalDateTime endDateTime = LocalDateTime.parse(historyReqDto.getEndDate(), formatter); // 종료 날짜 및 시간을 LocalDateTime으로 변환

            LocalDate startDate = startDateTime.toLocalDate();
            LocalDate endDate = endDateTime.toLocalDate();

            LocalTime startTime = startDateTime.toLocalTime();
            LocalTime endTime = endDateTime.toLocalTime();


            DailyTodo firstDayDailyTodo = dailyTodoRepository.findById(dailyTodoId).get();

            while (!startDate.isAfter(endDate)) {
                LocalDateTime startDateWithTime = startDate.atTime(startTime);
                LocalDateTime endDateWithTime;

                if (startDate.isEqual(endDate)) {
                    endDateWithTime = endDate.atTime(endTime);
                } else {
                    endDateWithTime = startDate.atTime(LocalTime.MAX);//23:59:59로 설정.
                }

                Duration duration = Duration.between(startDateWithTime, endDateWithTime);
                long totalSeconds = duration.getSeconds();

                DailyTodo dailyTodoToSave;
                if (startDate.equals(startDateTime.toLocalDate())) {//처음 날이면, 받은 dailyTodoId로 저장.
                    dailyTodoToSave = firstDayDailyTodo;
                }

                else{//새로운 dailyTodo만들고, 애와 연결된 history 저장.
                    DailyTodo dailyTodo = DailyTodo.builder()
                            .dailyTodoDate(startDate)
                            .status(DailyTodoStatusEnum.EMPTY)
                            .todo(firstDayDailyTodo.getTodo())
                            .user(firstDayDailyTodo.getUser())
                            .build();

                    dailyTodoToSave = dailyTodoRepository.save(dailyTodo);
                }
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
