package com.plot.plotserver.repository;

import com.plot.plotserver.domain.DailyTodo;
import com.plot.plotserver.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface DailyTodoRepository extends JpaRepository<DailyTodo, Long> {


    public Optional<DailyTodo> findById(Long id);

    @Query("SELECT dt FROM DailyTodo dt WHERE dt.todo.id = :todoId AND FUNCTION('DATE', dt.dailyTodoDate) = FUNCTION('DATE', :dailyTodoDate)")
    public Optional<DailyTodo> findByTodoIdAndDailyTodoDate(Long todoId, LocalDateTime dailyTodoDate);


}
