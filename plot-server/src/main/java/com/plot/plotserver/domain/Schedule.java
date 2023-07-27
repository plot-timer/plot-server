package com.plot.plotserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime endDate;

    @Column(name = "todo_id", nullable = false, columnDefinition = "bigint")
    private Long todoId;
}
