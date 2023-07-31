package com.plot.plotserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "varchar (36)")
    private String title;

    @Comment("속한 하위 카테고리 이름")
    @Column(name = "category_name", nullable = false, columnDefinition = "varchar (36)")
    private Long categoryName;

    @Column(name = "subtitle", nullable = true, columnDefinition = "varchar (36)")
    private String subTitle;

    @Column(name = "memo", nullable = true, columnDefinition = "varchar (36)")
    private String memo;

    @Column(name = "total_time", nullable = false, columnDefinition = "time")
    private Time totalTime;

    @Column(name = "start_date", nullable = false, columnDefinition = "datetime")
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = true, columnDefinition = "datetime")
    private LocalDateTime endDate;

    @Column(name = "cur_time", nullable = false, columnDefinition = "time")
    private Time curTime;

    @Column(name = "star", nullable = false, columnDefinition = "bit (1)")
    private boolean star;

    @Column(name = "icon_image_path", nullable = true, columnDefinition = "text")
    private String iconImagePath;

    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
    private Long categoryId;

}