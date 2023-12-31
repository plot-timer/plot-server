package com.plot.plotserver.domain;

import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.dto.request.todo.UpdateTodoDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "todo")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "varchar (36)")
    private String title;

    @Column(name = "subtitle", nullable = true, columnDefinition = "varchar (36)")
    private String subTitle;

    @Column(name = "memo", nullable = true, columnDefinition = "varchar (36)")
    private String memo;

    @Column(name = "star", nullable = false)//@Column(name = "star", nullable = false, columnDefinition = "bit (1)")
    private boolean star;

    @Column(name = "emoji", nullable = true)//@Column(name = "emoji", nullable = true, columnDefinition = "text")
    private String emoji;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DailyTodo> dailyTodos = new ArrayList<>();

    public void updateTodo(UpdateTodoDto reqDto, Category category){
        this.title=reqDto.getTitle();
        this.subTitle = reqDto.getSubtitle();
        this.memo = reqDto.getMemo();
        this.emoji = reqDto.getEmoji();
        this.star = reqDto.isStar();
        this.category = category;
    }

}