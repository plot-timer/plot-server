package com.plot.plotserver.domain;

import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar (36)")
    private String name;

    @Comment("즐겨찾기 여부")
    @Column(name = "star", nullable = false, columnDefinition = "bit (1)")//    @Column(name = "star", nullable = false)
    private boolean star;

    @Column(name = "emoji", nullable = true, columnDefinition = "text")//    @Column(name = "emoji", nullable = true)
    private String emoji;

    @ManyToOne
    @JoinColumn(name = "category_group_id",nullable = false)
    private CategoryGroup categoryGroup;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    private final List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    private final List<TagCategory> tagCategories = new ArrayList<>();

    public void updateCategory(UpdateCategoryReqDto reqDto, CategoryGroup categoryGroup){
        this.name = reqDto.getCategoryName();
        this.emoji = reqDto.getEmoji();
        this.star = reqDto.isStar();


        this.categoryGroup.deleteCategory(this);
        this.categoryGroup=categoryGroup;
        categoryGroup.addCategory(this);

    }

    @Builder
    public Category(String name,boolean star,String emoji,CategoryGroup categoryGroup){
        this.name=name;
        this.star=star;
        this.emoji=emoji;
        this.categoryGroup=categoryGroup;
    }


}