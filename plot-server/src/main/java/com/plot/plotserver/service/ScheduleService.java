package com.plot.plotserver.service;


import com.plot.plotserver.domain.DailyTodo;
import com.plot.plotserver.domain.Record;
import com.plot.plotserver.dto.request.history.HistoryReqDto;
import com.plot.plotserver.exception.history.HistorySavedFailException;
import com.plot.plotserver.repository.DailyTodoRepository;
import com.plot.plotserver.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final RecordRepository recordRepository;

    private final DailyTodoRepository dailyTodoRepository;


    public void showDailyTodosAndSchedulesForDate(LocalDate localDate) {

    }

    @Transactional
    public void save(Long dailyTodoId, HistoryReqDto historyReqDto) {

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(historyReqDto.getStartDate(), formatter); // 시작 날짜 및 시간을 LocalDateTime으로 변환
            LocalDateTime endDateTime = LocalDateTime.parse(historyReqDto.getEndDate(), formatter); // 종료 날짜 및 시간을 LocalDateTime으로 변환

            DailyTodo dailyTodo = dailyTodoRepository.findById(dailyTodoId).get();

            Duration duration = Duration.between(startDateTime, endDateTime); // 시간 간격 계산
            long totalSeconds = duration.getSeconds();

            Record record = Record.builder()
                    .isHistory(true)
                    .startDate(startDateTime)
                    .endDate(endDateTime)
                    .duration(totalSeconds)
                    .dailyTodo(dailyTodo)
                    .build();

            recordRepository.save(record);

        }catch(Exception e){
            throw new HistorySavedFailException("History 생성에 실패하였습니다.");
        }
    }

}
