package com.plot.plotserver.domain;

import lombok.*;

import javax.persistence.*;
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
    private LocalDateTime dailyTodoDate;

    @Column(name = "daily_history_total", nullable = true, columnDefinition = "datetime")
    private LocalDateTime dailyHistoryTotal;

    @Column(name = "daily_schedule_total", nullable = true, columnDefinition = "datetime")
    private LocalDateTime dailyScheduleTotal;



    @Column(name = "done", nullable = false, columnDefinition = "bit (1)")
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "todo_id",nullable = false)
    private Todo todo;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "dailyTodo", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Record> records = new ArrayList<>();
}

