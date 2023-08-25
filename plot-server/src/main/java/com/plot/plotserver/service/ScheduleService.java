package com.plot.plotserver.service;


import com.plot.plotserver.domain.DailyTodo;
import com.plot.plotserver.domain.Record;
import com.plot.plotserver.dto.request.Schedule.ScheduleReqDto;
import com.plot.plotserver.dto.response.schedule.ScheduleResponseDto;
import com.plot.plotserver.exception.history.HistorySavedFailException;
import com.plot.plotserver.exception.schedule.ScheduleNotFoundException;
import com.plot.plotserver.repository.DailyTodoRepository;
import com.plot.plotserver.repository.RecordRepository;
import com.plot.plotserver.util.SecurityContextHolderUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final RecordRepository recordRepository;

    private final DailyTodoRepository dailyTodoRepository;



    @Transactional
    public void save(List<ScheduleReqDto.Create> reqDtoList) {

        reqDtoList.forEach(reqDto -> {

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                LocalDateTime startDateTime = LocalDateTime.parse(reqDto.getStartDate(), formatter); // 시작 날짜 및 시간을 LocalDateTime으로 변환
                LocalDateTime endDateTime = LocalDateTime.parse(reqDto.getEndDate(), formatter); // 종료 날짜 및 시간을 LocalDateTime으로 변환

                DailyTodo dailyTodo = dailyTodoRepository.findById(reqDto.getDailyTodoId()).get();
                Duration duration = Duration.between(startDateTime, endDateTime);

                Record record = Record.builder()
                        .isHistory(false)
                        .startDate(startDateTime)
                        .endDate(endDateTime)
                        .duration(duration.getSeconds())
                        .dailyTodo(dailyTodo)
                        .build();

                recordRepository.save(record);

            }catch(Exception e){
                throw new HistorySavedFailException("Schedule 생성에 실패하였습니다.");
            }
        });
    }

    @Comment("해당 날짜에 따른 스케줄 목록 검색")
    public List<ScheduleResponseDto> searchByDate(ScheduleReqDto.GetScheduleList reqDto) {

        Long userId = SecurityContextHolderUtil.getUserId();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(reqDto.getDate(), formatter);

        List<ScheduleResponseDto> result = new ArrayList<>();

        // 해당 날짜에 존재하는 모든 daily_todo 탐색
        List<DailyTodo> dailyTodos = dailyTodoRepository.findByUserIDAndDate(userId, date);

        List<Record> schedules = new ArrayList<>();
        dailyTodos.forEach(dailyTodo -> schedules.addAll(recordRepository.findSchedulesByDailyTodoId(dailyTodo.getId())));

        schedules.forEach(schedule -> result.add(ScheduleResponseDto.of(schedule)));

        return result;
    }


    @Transactional
    public void updateSchedule(List<ScheduleReqDto.Update> reqDtoList) {

        reqDtoList.forEach(reqDto -> {
            Optional<Record> scheduleOpt = recordRepository.findSchedule(reqDto.getSchedule_id());

            if(!scheduleOpt.isPresent()){
                throw new ScheduleNotFoundException("Schedule ID 확인 요망");
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


            Record schedule = scheduleOpt.get();
            LocalDateTime updatedStartDate = LocalDateTime.parse(reqDto.getStartDate(), formatter);
            LocalDateTime updatedEndDate = LocalDateTime.parse(reqDto.getEndDate(), formatter);

            schedule.setStartDate(updatedStartDate);
            schedule.setEndDate(updatedEndDate);
            schedule.setDuration(Duration.between(updatedStartDate, updatedEndDate).toSeconds());
        });

    }


    public void deleteOne(Long scheduleId){

        Optional<Record> scheduleOpt = recordRepository.findSchedule(scheduleId);
        if(!scheduleOpt.isPresent()){
            throw new ScheduleNotFoundException("scheduleId 확인 요망.");
        }

        recordRepository.delete(scheduleOpt.get());
    }
}
