package com.plot.plotserver.domain;

import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.transaction.annotation.Transactional;

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
    @Column(name = "star", nullable = false)//    @Column(name = "star", nullable = false, columnDefinition = "bit (1)")//
    private boolean star;

    @Column(name = "emoji", nullable = true)//@Column(name = "emoji", nullable = true, columnDefinition = "text")
    private String emoji;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_group_id",nullable = false)
    private CategoryGroup categoryGroup;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    private final List<Todo> todos = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL,orphanRemoval = true)
    private final List<TagCategory> tagCategories = new ArrayList<>();

    @Transactional
    public void updateCategoryWithNewGroup(UpdateCategoryReqDto reqDto, CategoryGroup newCategoryGroup) {


        //기존 팀과 관계를 제거.
        if (this.categoryGroup != null) {
            this.categoryGroup.getCategories().remove(this);
        }

        this.categoryGroup = newCategoryGroup; // 카테고리 그룹 변경
        this.star = reqDto.isStar();//속성 변경.
        this.emoji = reqDto.getEmoji();
        this.name = reqDto.getCategoryName();

        newCategoryGroup.getCategories().add(this); // 새 그룹에 카테고리 추가

    }

    public void updateCategory(UpdateCategoryReqDto reqDto) {//그룹 그대로

        this.star = reqDto.isStar();
        this.emoji = reqDto.getEmoji();
        this.name = reqDto.getCategoryName();
    }


    @Builder
    public Category(String name,boolean star,String emoji,CategoryGroup categoryGroup){
        this.name=name;
        this.star=star;
        this.emoji=emoji;
        this.categoryGroup=categoryGroup;
    }


}