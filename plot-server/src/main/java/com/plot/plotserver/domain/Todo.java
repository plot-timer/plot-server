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
    @Column(name = "todo_id")
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "varchar (36)")
    private String title;

//    @Comment("속한 하위 카테고리 이름")
//    @Column(name = "category_name", nullable = false, columnDefinition = "varchar (36)")
//    private String categoryName;

    @Column(name = "subtitle", nullable = true, columnDefinition = "varchar (36)")
    private String subTitle;

    @Column(name = "memo", nullable = true, columnDefinition = "varchar (36)")
    private String memo;


    @Column(name = "star", nullable = false, columnDefinition = "bit (1)")
    private boolean star;

    @Column(name = "emoji", nullable = true, columnDefinition = "text")
    private String emoji;

    @Column(name = "done",nullable = false,columnDefinition = "bit (1)")
    private boolean done;

//    @Column(name = "user_id", nullable = false, columnDefinition = "bigint")
//    private Long userId;

    @ManyToOne
    @JoinColumn(name = "users_id",nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

//    @Column(name = "category_id", nullable = false, columnDefinition = "bigint")
//    private Long categoryId;

}