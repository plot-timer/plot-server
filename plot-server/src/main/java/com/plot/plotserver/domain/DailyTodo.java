package com.plot.plotserver.domain;

import com.plot.plotserver.dto.request.DailyTodo.UpdateDailyTodoReqDto;
import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.util.ColorEnum;
import com.plot.plotserver.util.DailyTodoStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "daily_todo")
public class DailyTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "daily_todo_date", nullable = false, columnDefinition = "datetime")
    private LocalDate dailyTodoDate;


    @Column(name = "done", nullable = false, columnDefinition = "bit (1)")
    private boolean done;

    @Column(name = "status", nullable = false)
    @Enumerated
    private DailyTodoStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "todo_id",nullable = false)
    private Todo todo;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "dailyTodo", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Record> records = new ArrayList<>();

    public void updateDailyTodo(UpdateDailyTodoReqDto reqDto){
        this.status = reqDto.getStatus();
    }
}

