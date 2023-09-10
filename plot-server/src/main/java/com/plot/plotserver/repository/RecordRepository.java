package com.plot.plotserver.repository;

import com.plot.plotserver.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {


    @Query("SELECT r FROM Record r WHERE r.dailyTodo.id = :dailyTodoId")
    List<Record> findByDailyTodoId(Long dailyTodoId);

    @Query("SELECT r FROM Record r WHERE r.isHistory = false AND r.id = :scheduleId")
    Optional<Record> findSchedule(Long scheduleId);

    @Query("SELECT r FROM Record r WHERE r.isHistory = true AND r.dailyTodo.id = :dailyTodoId")
    List<Record> findHistoriesByDailyTodoId(Long dailyTodoId);

    @Query("SELECT r FROM Record r WHERE r.isHistory = false AND r.dailyTodo.id = :dailyTodoId")
    List<Record> findSchedulesByDailyTodoId(Long dailyTodoId);

    @Query("SELECT r FROM Record r WHERE r.isHistory = false AND r.scheduledDate = :date AND r.dailyTodo.user.id = :userId")
    List<Record> findSchedulesByDateAndUser(Long userId, LocalDate date);

}
