package com.plot.plotserver.domain;

import com.plot.plotserver.dto.request.category.UpdateCategoryReqDto;
import com.plot.plotserver.dto.request.categorygroup.UpdateCategoryGroupReqDto;
import com.plot.plotserver.util.ColorEnum;
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
@Table(name = "category_group")
public class CategoryGroup {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar (36)")
    private String name;

    @Column(name = "color", nullable = false)
    @Enumerated
    private ColorEnum color;

    @ManyToOne
    @JoinColumn(name = "users_id",nullable = false)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "categoryGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private  List<Category> categories = new ArrayList<>();

    @Builder
    public CategoryGroup(String name,ColorEnum color,User user){
        this.name=name;
        this.color=color;
        this.user=user;
    }

    public void updateCategoryGroup(UpdateCategoryGroupReqDto reqDto){
        this.name = reqDto.getGroupName();
        this.color = reqDto.getColor();
    }
    public void addCategory(Category category) {// 양방향 매핑
        categories.add(category);
    }

    public void deleteCategory(Category category) {
        categories.remove(category);
    }

}