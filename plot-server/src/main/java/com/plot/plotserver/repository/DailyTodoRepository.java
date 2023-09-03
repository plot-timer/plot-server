package com.plot.plotserver.repository;

import com.plot.plotserver.domain.DailyTodo;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface DailyTodoRepository extends JpaRepository<DailyTodo, Long> {


    @Comment("DailyTodo 만 가져오기")
    public Optional<DailyTodo> findById(Long id);

    @Comment("DailyTodo,그의 부모 Todo, Category, CategoryGroup 까지 모두 조회.")
    @Query("SELECT dt FROM DailyTodo dt " +
            "JOIN FETCH dt.todo t " +
            "JOIN FETCH t.category c " +
            "JOIN FETCH c.categoryGroup cg " +
            "WHERE dt.id = :id")
    Optional<DailyTodo> findByIdJoinTodoAndCategoryAndCategoryGroup(@Param("id") Long id);

    @Comment("DailyTodo 만 가져오기")
    @Query("SELECT dt FROM DailyTodo dt WHERE dt.todo.id = :todoId AND dt.dailyTodoDate = :dailyTodoDate")
    public Optional<DailyTodo> findByTodoIdAndDailyTodoDate(@Param("todoId") Long todoId, @Param("dailyTodoDate")LocalDate dailyTodoDate);

    @Comment("DailyTodo 만 가져오기")
    @Query("SELECT dt FROM DailyTodo dt WHERE dt.user.id =:userId AND dt.dailyTodoDate = :dailyTodoDate")
    public List<DailyTodo> findByUserIDAndDate(@Param("userId") Long userId, @Param("dailyTodoDate") LocalDate dailyTodoDate);


    @Comment("DailyTodo, 그의 부모 Todo, Category, CategoryGroup 까지 모두 조회.")
    @Query("SELECT dt FROM DailyTodo dt " +
            "JOIN FETCH dt.todo t " +
            "JOIN FETCH t.category c " +
            "JOIN FETCH c.categoryGroup cg " +
            "WHERE cg.user.id = :userId AND dt.dailyTodoDate = :dailyTodoDate")
    public List<DailyTodo> findByUserIDAndDateJoinTodoAndCategoryAndCategoryGroup(@Param("userId") Long userId, @Param("dailyTodoDate") LocalDate dailyTodoDate);






}
