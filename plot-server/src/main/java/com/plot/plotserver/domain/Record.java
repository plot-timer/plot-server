package com.plot.plotserver.domain;

import lombok.*;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "record")
public class Record {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime endDate;

    @Comment("히스토리/스케줄을 결정하는 필드. 1: 히스토리, 0: 스케줄을 의미")
    @Column(name = "is_history", nullable = false, columnDefinition = "bit(1)")
    private boolean isHistory;

    @Comment("걸린 시간 초단위")
    @Column(name = "duration", nullable = false)
    private Long duration;

    @ManyToOne
    @JoinColumn(name = "daily_todo_id",nullable = false)
    private DailyTodo dailyTodo;

}